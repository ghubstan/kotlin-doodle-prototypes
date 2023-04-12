package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.Strength
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class Menu(
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
    private val eventBus: MenuEventBus
) : View() {

    private val mainScope = MainScope() // the scope of Menu class, uses Dispatchers.Main.

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuIcon = LazyPhoto(mainScope.async { images.load("drawer-menu.svg")!! })

    private val menuPopup = MenuPopup(
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
        eventBus
    ).apply {
        pointerChanged += clicked { popups.hide(this) }
    }

    init {
        size = Size(100, 100)
        children += listOf(menuIcon)
        layout = constrain(menuIcon, fill)

        pointerChanged += clicked {
            popups.show(menuPopup, relativeTo = this) { menuPopupBounds, mainViewBounds ->
                // Size and position popup.
                (menuPopupBounds.top eq mainViewBounds.y)..Strength.Strong
                (menuPopupBounds.left eq mainViewBounds.right + 10)..Strength.Strong
                (menuPopupBounds.bottom lessEq mainViewBounds.bottom - 5)..Strength.Strong

                /*
                menuPopupBounds.top greaterEq 5
                menuPopupBounds.left greaterEq 5
                menuPopupBounds.right lessEq parent.right - 5
                menuPopupBounds.height eq 290 // Do not allow menu links to be hidden during vertical resize.
                menuPopupBounds.width eq parent.width * 0.20
                 */

                menuPopupBounds.top eq 5
                menuPopupBounds.left eq parent.right - parent.right * 0.45
                menuPopupBounds.right eq parent.right - parent.right * 0.08
                menuPopupBounds.height eq 290 // Do not allow menu links to be hidden during vertical resize.
            }
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.Transparent)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}