package io.dongxi.page.panel.menu

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.MenuEventBus
import io.dongxi.util.ColorUtils.colorPaleSilver
import io.dongxi.util.ColorUtils.colorSilver
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MenuPopupButtons(
    private val config: DongxiConfig,
    private val uiDispatcher: CoroutineDispatcher,
    private val animator: Animator,
    private val pathMetrics: PathMetrics,
    private val fonts: FontLoader,
    private val theme: DynamicTheme,
    private val themes: ThemeManager,
    private val images: ImageLoader,
    private val textMetrics: TextMetrics,
    private val linkStyler: NativeHyperLinkStyler,
    private val focusManager: FocusManager,
    private val popups: PopupManager,
    private val modals: ModalManager,
    private val menuEventBus: MenuEventBus
) : View() {

    private val mainScope = MainScope()

    private val homeButton = createMenuButton(
        config = config,
        buttonText = "Casa",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_HOME,
        menuEventBus = menuEventBus,
    )
    private val registerButton = createMenuButton(
        config = config,
        buttonText = "Crie a Sua Conta",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_REGISTER,
        menuEventBus = menuEventBus,
    )
    private val loginButton = createMenuButton(
        config = config,
        buttonText = "Entre",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_LOGIN,
        menuEventBus = menuEventBus,
    )
    private val basketButton = createMenuButton(
        config = config,
        buttonText = "Carrihno de Compras",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_BASKET,
        menuEventBus = menuEventBus,
    )
    private val paymentButton = createMenuButton(
        config = config,
        buttonText = "Pagamento",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_PAYMENT,
        menuEventBus = menuEventBus,
    )
    private val logoutButton = createMenuButton(
        config = config,
        buttonText = "Sair",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_LOGOUT,
        menuEventBus = menuEventBus,
    )

    private val buttonOffset = 0 // No space between bottoms & tops of buttons
    private val buttonHeight = 26

    init {
        size = Size(160, 600)
        children += listOf(
            homeButton,
            registerButton,
            loginButton,
            basketButton,
            paymentButton,
            logoutButton
        )
        layout = constrain(
            homeButton,
            registerButton,
            loginButton,
            basketButton,
            paymentButton,
            logoutButton
        ) { (homeButtonBounds,
                registerButtonBounds,
                loginButtonBounds,
                basketButtonBounds,
                paymentButtonBounds,
                logoutButtonBounds) ->

            homeButtonBounds.top eq 2
            homeButtonBounds.left eq buttonOffset
            homeButtonBounds.right eq parent.right
            homeButtonBounds.height eq buttonHeight

            registerButtonBounds.top eq homeButtonBounds.bottom + buttonOffset
            registerButtonBounds.left eq buttonOffset
            registerButtonBounds.right eq parent.right
            registerButtonBounds.height eq buttonHeight

            loginButtonBounds.top eq registerButtonBounds.bottom + buttonOffset
            loginButtonBounds.left eq buttonOffset
            loginButtonBounds.right eq parent.right
            loginButtonBounds.height eq buttonHeight

            basketButtonBounds.top eq loginButtonBounds.bottom + buttonOffset
            basketButtonBounds.left eq buttonOffset
            basketButtonBounds.right eq parent.right
            basketButtonBounds.height eq buttonHeight

            paymentButtonBounds.top eq basketButtonBounds.bottom + buttonOffset
            paymentButtonBounds.left eq buttonOffset
            paymentButtonBounds.right eq parent.right
            paymentButtonBounds.height eq buttonHeight

            logoutButtonBounds.top eq paymentButtonBounds.bottom + buttonOffset
            logoutButtonBounds.left eq buttonOffset
            logoutButtonBounds.right eq parent.right
            logoutButtonBounds.height eq buttonHeight
        }
        pointerChanged += clicked { popups.hide(this) }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Transparent)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    private fun createMenuButton(
        config: DongxiConfig,
        buttonText: String,
        tooltipText: String?,
        textMetrics: TextMetrics,
        focusManager: FocusManager,
        menuEvent: MenuEvent,
        menuEventBus: MenuEventBus
    ): PushButton {

        return PushButton(buttonText).apply {
            acceptsThemes = false
            size = Size(113, 40)
            cursor = Cursor.Pointer
            toolTipText = tooltipText ?: ""
            horizontalAlignment = Center
            verticalAlignment = Middle
            behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->

                when {
                    button.model.pointerOver -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Black, thickness = 6.0),
                        color = colorPaleSilver(),
                        radius = 10.0
                    )

                    else -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Darkgray, thickness = 3.0),
                        color = colorSilver(),
                        radius = 10.0
                    )
                }

                canvas.text(
                    text = button.text,
                    at = textPosition(button, button.text),
                    fill = Black.paint,
                    font = font
                )
            }
            fired += {
                mainScope.launch {
                    menuEventBus.produceEvent(menuEvent)
                }
            }
        }
    }


    // Helper to use constrain with 6 items
    private operator fun <T> List<T>.component6() = this[5]

    // Helper to use constrain with 7 items
    private operator fun <T> List<T>.component7() = this[6]

    // Helper to use constrain with 8 items
    private operator fun <T> List<T>.component8() = this[7]

    // Helper to use constrain with 9 items
    private operator fun <T> List<T>.component9() = this[8]

    // Helper to use constrain with 10 items
    private operator fun <T> List<T>.component10() = this[9]

    // Helper to use constrain with 11 items
    private operator fun <T> List<T>.component11() = this[10]

    // Helper to use constrain with 12 items
    private operator fun <T> List<T>.component12() = this[11]
}