package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.dongxi.view.MenuEvent.*
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

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

    private val homeButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Casa",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_HOME,
        menuEventBus = menuEventBus,
    )
    private val ringsButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Aneis",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_RINGS,
        menuEventBus = menuEventBus,
    )
    private val earringsButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Brincos",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_EAR_RINGS,
        menuEventBus = menuEventBus,
    )
    private val necklacesButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Colares",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_NECKLACES,
        menuEventBus = menuEventBus,
    )
    private val scapularsButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Escapulários",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_SCAPULARS,
        menuEventBus = menuEventBus,
    )
    private val braceletsButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Pulseiras",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_BRACELETS,
        menuEventBus = menuEventBus,
    )
    private val aboutButton = MenuButtonFactory.createMenuButton(
        config = config,
        buttonText = "Sobre",
        tooltipText = null,
        textMetrics = textMetrics,
        focusManager = focusManager,
        GO_ABOUT,
        menuEventBus = menuEventBus,
    )

    private val buttonOffset = 0 // No space between bottoms & tops of buttons
    private val buttonHeight = 24

    init {
        size = Size(160, 600)
        children += listOf(
            homeButton,
            ringsButton,
            earringsButton,
            necklacesButton,
            scapularsButton,
            braceletsButton,
            aboutButton
        )
        layout = constrain(
            homeButton,
            ringsButton,
            earringsButton,
            necklacesButton,
            scapularsButton,
            braceletsButton,
            aboutButton
        ) { (homeButtonBounds,
                ringsButtonBounds,
                earringsButtonBounds,
                necklacesButtonBounds,
                scapularsButtonBounds,
                braceletsButtonBounds,
                aboutButtonBounds) ->

            // homeButtonBounds.top eq 40 // Avoid overlapping page name label.
            homeButtonBounds.top eq 2
            homeButtonBounds.left eq 25
            homeButtonBounds.right eq parent.right - 5
            homeButtonBounds.height eq buttonHeight

            ringsButtonBounds.top eq homeButtonBounds.bottom + buttonOffset
            ringsButtonBounds.left eq 25
            ringsButtonBounds.right eq parent.right - 5
            ringsButtonBounds.height eq buttonHeight

            earringsButtonBounds.top eq ringsButtonBounds.bottom + buttonOffset
            earringsButtonBounds.left eq 25
            earringsButtonBounds.right eq parent.right - 5
            earringsButtonBounds.height eq buttonHeight

            necklacesButtonBounds.top eq earringsButtonBounds.bottom + buttonOffset
            necklacesButtonBounds.left eq 25
            necklacesButtonBounds.right eq parent.right - 5
            necklacesButtonBounds.height eq buttonHeight

            scapularsButtonBounds.top eq necklacesButtonBounds.bottom + buttonOffset
            scapularsButtonBounds.left eq 25
            scapularsButtonBounds.right eq parent.right - 5
            scapularsButtonBounds.height eq buttonHeight

            braceletsButtonBounds.top eq scapularsButtonBounds.bottom + buttonOffset
            braceletsButtonBounds.left eq 25
            braceletsButtonBounds.right eq parent.right - 5
            braceletsButtonBounds.height eq buttonHeight

            aboutButtonBounds.top eq braceletsButtonBounds.bottom + buttonOffset
            aboutButtonBounds.left eq 25
            aboutButtonBounds.right eq parent.right - 5
            aboutButtonBounds.height eq buttonHeight

        }
        pointerChanged += clicked { popups.hide(this) }
    }

    override fun render(canvas: Canvas) {
        // From https://www.colorhexa.com/color-names
        val antiFlashWhite = Color(0xf2f3f4u)
        val floralWhite = Color(0xfffaf0u)
        val ghostWhite = Color(0xf8f8ffu)
        val whiteSmoke = Color(0xf5f5f5u)
        canvas.rect(bounds.atOrigin, Color.Transparent)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
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