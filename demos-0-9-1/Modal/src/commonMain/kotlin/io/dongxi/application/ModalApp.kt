package io.dongxi.application

import io.nacular.doodle.animation.Animation
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.animation.invoke
import io.nacular.doodle.animation.transition.easeOutBack
import io.nacular.doodle.animation.transition.easeOutBounce
import io.nacular.doodle.animation.transition.linear
import io.nacular.doodle.animation.tweenFloat
import io.nacular.doodle.application.Application
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.core.Display
import io.nacular.doodle.core.View
import io.nacular.doodle.core.center
import io.nacular.doodle.core.then
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.AffineTransform.Companion.Identity
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.drawing.Color.Companion.White
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.TextAlignment
import io.nacular.doodle.utils.autoCanceling
import io.nacular.doodle.utils.lerp
import io.nacular.measured.units.Angle.Companion.degrees
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.times
import kotlinx.coroutines.*

/**
 * See https://nacular.github.io/doodle/docs/modals
 *
 * "Thanks for clicking. Now please press Ok to acknowledge"
 */
class ModalApp(
    display: Display,
    uiDispatcher: CoroutineDispatcher,
    animator: Animator,
    pathMetrics: PathMetrics,
    fonts: FontLoader,
    theme: DynamicTheme,
    themes: ThemeManager,
    images: ImageLoader,
    textMetrics: TextMetrics,
    linkStyler: NativeHyperLinkStyler,
    focusManager: FocusManager,
    popups: PopupManager,
    modals: ModalManager
) : Application {

    init {
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        appScope.launch(uiDispatcher) {
            val titleFont = fonts {
                size = 18; weight = 100; families = listOf("Helvetica Neue", "Helvetica", "Arial", "sans-serif")
            }!!
            val listFont = fonts(titleFont) { size = 14 }!!
            val tabPanelFont = fonts(titleFont) { size = 12; weight = 400 }!!
            val footerFont = fonts(titleFont) { size = 10 }!!
            val config = DongxiConfig(
                listFont = listFont,
                titleFont = titleFont,
                tabPanelFont = tabPanelFont,
                footerFont = footerFont,
                filterFont = fonts(titleFont) { size = 14 }!!,
                boldFooterFont = fonts(footerFont) { weight = 400 }!!,
                placeHolderFont = fonts(listFont) { style = Font.Style.Italic }!!,
                checkForeground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23bddad5%22%20stroke-width%3D%223%22/%3E%3Cpath%20fill%3D%22%235dc2af%22%20d%3D%22M72%2025L42%2071%2027%2056l-4%204%2020%2020%2034-52z%22/%3E%3C/svg%3E")!!,
                checkBackground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E")!!
            )

            themes.selected = theme

            val clickMe = ClickMeView(
                config,
                uiDispatcher,
                animator,
                pathMetrics,
                fonts,
                theme,
                themes,
                images,
                textMetrics,
                linkStyler,
                focusManager,
                popups,
                modals
            ).apply {
                pointerChanged += clicked {
                    appScope.launch {
                        modals {
                            val popup = createPopup(textMetrics = textMetrics, completed = this::completed).also {
                                it.font = font
                            }
                            val duration = 250 * milliseconds

                            var jiggleAnimation: Animation<*>? by autoCanceling()

                            // Shake the popup when the pointer clicked outside of it
                            pointerOutsideModalChanged += clicked {
                                jiggleAnimation = animator(1f to 0f, tweenFloat(easeOutBounce, duration)) {
                                    popup.transform = Identity.rotate(around = popup.center, by = 2 * degrees * it)
                                }
                            }

                            var layoutProgress = 0f

                            animator {
                                // Animate background
                                0f to 1f using (tweenFloat(linear, duration)) {
                                    background = lerp(Lightgray opacity 0f, Lightgray opacity 0.5f, it).paint
                                }

                                // Animate layout
                                0f to 1f using (tweenFloat(easeOutBack, duration)) {
                                    layoutProgress = it // modify values used in layout
                                    reLayout()          // ask modal to update its layout
                                }
                            }

                            // Show modal with popup using the given layout constraints
                            ModalManager.Modal(popup) {
                                it.centerX eq parent.centerX
                                it.centerY eq lerp(-it.height.readOnly / 2, parent.centerY.readOnly, layoutProgress)
                            }
                        }
                    }
                }
            }

            display += listOf(clickMe)
            display.fill(config.appBackground.paint)
            display.layout = constrain(display.first(), fill)
        }
    }

    private fun createPopup(textMetrics: TextMetrics, completed: (Unit) -> Unit) = object : View() {
        init {
            width = 300.0
            height = 300.0
            clipCanvasToBounds = false

            children += Label("Thanks for clicking. Now please press Ok to acknowledge.").apply {
                fitText = setOf(Dimension.Height)
                wrapsWords = true
                textAlignment = TextAlignment.Start
            }

            children += PushButton("OK").apply {
                size = Size(100, 40)
                cursor = Cursor.Pointer
                acceptsThemes = false // Set false when using inline behaviors.
                toolTipText = "Push Me"
                behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
                    when {
                        button.model.pointerOver -> canvas.rect(
                            bounds.atOrigin,
                            stroke = Stroke(color = Color.Darkgray, thickness = 6.0),
                            color = Color.Cyan
                        )

                        else -> canvas.rect(
                            bounds.atOrigin,
                            stroke = Stroke(color = Black, thickness = 3.0),
                            color = Color.Orange
                        )
                    }
                    canvas.text(button.text, at = textPosition(button, button.text), fill = Black.paint, font = font)
                }

                fired += { completed(Unit) }
            }

            layout = constrain(children[0], children[1]) { label, button ->
                label.top eq 20
                label.left eq 20
                label.right eq parent.right - 20
                label.height.preserve

                button.top eq label.bottom + 20
                button.width eq parent.width / 4
                button.height eq 30
                button.centerX eq parent.centerX
            }.then {
                height = children.last().bounds.bottom + 20
            }
        }

        override fun render(canvas: Canvas) {
            canvas.outerShadow(blurRadius = 10.0, color = Black opacity 0.05f) {
                canvas.rect(bounds.atOrigin, radius = 10.0, fill = White.paint)
            }
        }

        fun shutdown() {
            TODO("Not implemented")
        }
    }


    override fun shutdown() {
        TODO("Not implemented")
    }
}