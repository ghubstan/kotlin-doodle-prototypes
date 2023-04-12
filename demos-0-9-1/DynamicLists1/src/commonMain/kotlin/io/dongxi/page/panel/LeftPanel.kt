package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.page.panel.event.BaseProductSelectEvent.SELECT_BASE_RING
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.HorizontalFlowLayout
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class LeftPanel(
    config: DongxiConfig,
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
    modals: ModalManager,
    baseProductSelectEventBus: BaseProductSelectEventBus
) : AbstractPanel(
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
    modals,
    baseProductSelectEventBus
) {

    private val button1 = fakeEventButton("Ring 1", "Select Ring 1")
    private val button2 = fakeEventButton("Ring 2", "Select Ring 2")
    private val button3 = fakeEventButton("Ring 3", "Select Ring 3")

    init {
        clipCanvasToBounds = false

        size = Size(200, 200)

        children += listOf(button1, button2, button3)
        layout = HorizontalFlowLayout()
        // layout = constrain(button1, button2, button3) { button1Bounds, button2Bounds, button3Bounds -> }
    }

    private fun fakeEventButton(buttonText: String, buttonTooltip: String): PushButton {
        return PushButton(buttonText).apply {
            size = Size(150, 40)
            cursor = Cursor.Pointer
            acceptsThemes = false // Set false when using inline behaviors.
            toolTipText = buttonTooltip
            behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
                when {
                    button.model.pointerOver -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Darkgray, thickness = 6.0),
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
            fired += {
                // Force an event to see if BaseGridPanel can respond.
                mainScope.launch {
                    SELECT_BASE_RING.setBaseProductDetail(buttonText, "$buttonText File", null)
                    currentBaseProduct = SELECT_BASE_RING.baseProductDetail()
                    baseProductSelectEventBus.produceEvent(SELECT_BASE_RING)

                    println("LeftPanel currentBaseProduct: $currentBaseProduct")
                }
            }
        }
    }
}
