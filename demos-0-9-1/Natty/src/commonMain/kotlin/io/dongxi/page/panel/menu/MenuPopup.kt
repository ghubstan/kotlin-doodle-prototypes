package io.dongxi.page.panel.menu

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.MenuEventBus
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class MenuPopup(config: DongxiConfig, val commonDI: DI) : View() {

    private val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()

    private val mainScope = MainScope()

    private val homeButton = createMenuButton(config = config, buttonText = "Home", tooltipText = null, GO_HOME)
    private val ringsButton = createMenuButton(config = config, buttonText = "Aneis", tooltipText = null, GO_RINGS)
    private val earringsButton = createMenuButton(config = config, buttonText = "Brincos", tooltipText = null, GO_EARRINGS)
    private val necklacesButton = createMenuButton(config = config, buttonText = "Colares", tooltipText = null, GO_NECKLACES)
    private val scapularsButton = createMenuButton(config = config, buttonText = "Escapulários", tooltipText = null, GO_SCAPULARS)
    private val braceletsButton = createMenuButton(config = config, buttonText = "Pulseiras", tooltipText = null, GO_BRACELETS)
    private val aboutButton = createMenuButton(config = config, buttonText = "Sobre", tooltipText = null, GO_ABOUT)
    private val registerButton = createMenuButton(config = config, buttonText = "Crie a Sua Conta", tooltipText = null, GO_REGISTER)
    private val loginButton = createMenuButton(config = config, buttonText = "Entre", tooltipText = null, GO_LOGIN)
    private val basketButton = createMenuButton(config = config, buttonText = "Carrihno de Compras", tooltipText = null, GO_BASKET)
    private val paymentButton = createMenuButton(config = config, buttonText = "Pagamento", tooltipText = null, GO_PAYMENT)
    private val logoutButton = createMenuButton(config = config, buttonText = "Sair", tooltipText = null, GO_LOGOUT)

    private val buttonOffset = 0 // No space between bottoms & tops of buttons
    private val buttonHeight = 26

    init {
        size = Size(160, 600)
        children += listOf(
            homeButton,
            ringsButton,
            earringsButton,
            necklacesButton,
            scapularsButton,
            braceletsButton,
            aboutButton,
            registerButton
        )
        layout = constrain(
            homeButton,
            ringsButton,
            earringsButton,
            necklacesButton,
            scapularsButton,
            braceletsButton,
            aboutButton,
            registerButton
        ) { (homeButtonBounds,
                ringsButtonBounds,
                earringsButtonBounds,
                necklacesButtonBounds,
                scapularsButtonBounds,
                braceletsButtonBounds,
                aboutButtonBounds,
                registerButtonBounds) ->

            homeButtonBounds.top eq 2
            homeButtonBounds.left eq buttonOffset
            homeButtonBounds.right eq parent.right
            homeButtonBounds.height eq buttonHeight

            ringsButtonBounds.top eq homeButtonBounds.bottom + buttonOffset
            ringsButtonBounds.left eq buttonOffset
            ringsButtonBounds.right eq parent.right
            ringsButtonBounds.height eq buttonHeight

            earringsButtonBounds.top eq ringsButtonBounds.bottom + buttonOffset
            earringsButtonBounds.left eq buttonOffset
            earringsButtonBounds.right eq parent.right
            earringsButtonBounds.height eq buttonHeight

            necklacesButtonBounds.top eq earringsButtonBounds.bottom + buttonOffset
            necklacesButtonBounds.left eq buttonOffset
            necklacesButtonBounds.right eq parent.right
            necklacesButtonBounds.height eq buttonHeight

            scapularsButtonBounds.top eq necklacesButtonBounds.bottom + buttonOffset
            scapularsButtonBounds.left eq buttonOffset
            scapularsButtonBounds.right eq parent.right
            scapularsButtonBounds.height eq buttonHeight

            braceletsButtonBounds.top eq scapularsButtonBounds.bottom + buttonOffset
            braceletsButtonBounds.left eq buttonOffset
            braceletsButtonBounds.right eq parent.right
            braceletsButtonBounds.height eq buttonHeight

            aboutButtonBounds.top eq braceletsButtonBounds.bottom + buttonOffset
            aboutButtonBounds.left eq buttonOffset
            aboutButtonBounds.right eq parent.right
            aboutButtonBounds.height eq buttonHeight

            registerButtonBounds.top eq aboutButtonBounds.bottom + buttonOffset
            registerButtonBounds.left eq buttonOffset
            registerButtonBounds.right eq parent.right
            registerButtonBounds.height eq buttonHeight
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
        //textMetrics: TextMetrics,
        //focusManager: FocusManager,
        menuEvent: MenuEvent,
        //menuEventBus: MenuEventBus
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
                        stroke = Stroke(color = Black, thickness = 5.0),
                        color = config.selectedMenuButtonColor,
                        radius = 10.0
                    )

                    else -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Darkgray, thickness = 2.5),
                        color = config.appBackgroundColor,
                        radius = 10.0
                    )
                }

                canvas.text(
                    text = button.text,  // TODO Need StyledText?
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