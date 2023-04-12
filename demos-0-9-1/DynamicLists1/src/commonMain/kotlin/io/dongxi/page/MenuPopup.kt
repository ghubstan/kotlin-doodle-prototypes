package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent.*
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.HyperLink
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
import kotlinx.coroutines.launch

class MenuPopup(
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

    private val mainScope = MainScope() // the scope of Menu class, uses Dispatchers.Main.

    private val homeLink: HyperLink = createMenuLink("casa?", "Casa", GO_HOME)
    private val ringsLink: HyperLink = createMenuLink("rings?", "Aneis", GO_RINGS)
    private val necklacesLink: HyperLink = createMenuLink("necklaces?", "Colares", GO_NECKLACES)
    private val scapularsLink: HyperLink = createMenuLink("scapulars?", "Escapulários", GO_SCAPULARS)
    private val braceletsLink: HyperLink = createMenuLink("bracelets?", "Pulseiras", GO_BRACELETS)
    private val earRingsLink: HyperLink = createMenuLink("earrings?", "Brincos", GO_EARRINGS)
    private val aboutLink: HyperLink = createMenuLink("about?", "Sobre", GO_ABOUT)

    private fun createMenuLink(url: String, text: String, menuEvent: MenuEvent): HyperLink {
        return HyperLink(
            url = url,
            text = text
        ).apply {
            font = config.menuLinkFont
            fired += {
                mainScope.launch {
                    menuEventBus.produceEvent(menuEvent)
                }
            }
        }
    }

    private val linkOffset = 10
    private val verticalLinkSpacing = 24

    init {
        size = Size(160, 600)
        children += listOf(
            homeLink, ringsLink,
            necklacesLink, scapularsLink,
            braceletsLink, earRingsLink, aboutLink
        )
        layout = constrain(
            homeLink, ringsLink,
            necklacesLink, scapularsLink, braceletsLink, earRingsLink, aboutLink
        ) { (homeLinkBounds, ringsLinkBounds,
                necklacesLinkBounds, scapularsLinkBounds, braceletsLinkBounds,
                earRingsLinkBounds, aboutLinkBounds) ->

            homeLinkBounds.top eq 40 // Avoid overlapping page name label.
            homeLinkBounds.left eq linkOffset
            homeLinkBounds.height eq verticalLinkSpacing

            ringsLinkBounds.top eq homeLinkBounds.bottom + linkOffset
            ringsLinkBounds.left eq linkOffset
            ringsLinkBounds.height eq verticalLinkSpacing

            necklacesLinkBounds.top eq ringsLinkBounds.bottom + linkOffset
            necklacesLinkBounds.left eq linkOffset
            necklacesLinkBounds.height eq verticalLinkSpacing

            scapularsLinkBounds.top eq necklacesLinkBounds.bottom + linkOffset
            scapularsLinkBounds.left eq linkOffset
            scapularsLinkBounds.height eq verticalLinkSpacing

            braceletsLinkBounds.top eq scapularsLinkBounds.bottom + linkOffset
            braceletsLinkBounds.left eq linkOffset
            braceletsLinkBounds.height eq verticalLinkSpacing


            earRingsLinkBounds.top eq braceletsLinkBounds.bottom + linkOffset
            earRingsLinkBounds.left eq linkOffset
            earRingsLinkBounds.height eq verticalLinkSpacing

            aboutLinkBounds.top eq earRingsLinkBounds.bottom + linkOffset
            aboutLinkBounds.left eq linkOffset
            aboutLinkBounds.height eq verticalLinkSpacing

        }
        pointerChanged += clicked { popups.hide(this) }
    }

    override fun render(canvas: Canvas) {
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