package io.dongxi.page.panel.menu

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class Menu(
    val config: DongxiConfig,
    val uiDispatcher: CoroutineDispatcher,
    val animator: Animator,
    val pathMetrics: PathMetrics,
    val fonts: FontLoader,
    val theme: DynamicTheme,
    val themes: ThemeManager,
    val images: ImageLoader,
    val textMetrics: TextMetrics,
    val textFieldStyler: NativeTextFieldStyler,
    val linkStyler: NativeHyperLinkStyler,
    val focusManager: FocusManager,
    val popups: PopupManager,
    val modals: ModalManager,
    val menuEventBus: MenuEventBus
) : View() {

    private val mainScope = MainScope() // the scope of Menu class, uses Dispatchers.Main.

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuIcon = LazyPhoto(mainScope.async { images.load("drawer-menu.svg")!! })

    private val menuPopupLinks = MenuPopupButtons(
        config,
        uiDispatcher,
        animator,
        pathMetrics,
        fonts,
        theme,
        themes,
        images,
        textMetrics,
        textFieldStyler,
        linkStyler,
        focusManager,
        popups,
        modals,
        menuEventBus
    ).apply {
        pointerChanged += clicked { popups.hide(this) }
    }

    init {
        size = Size(55, 55)
        children += listOf(menuIcon)
        layout = constrain(menuIcon) { menuIconBounds ->
            menuIconBounds.top eq 0
            menuIconBounds.left eq parent.right - 55
            menuIconBounds.width.preserve
            menuIconBounds.height.preserve
        }

        pointerChanged += clicked {
            popups.show(menuPopupLinks, relativeTo = this) { menuPopupBounds, _ ->
                menuPopupBounds.top eq 5
                menuPopupBounds.left eq parent.width / 2  // Buttons extend left to cover 1/2 of screen.
                menuPopupBounds.right eq parent.right - 5
                menuPopupBounds.height eq 290
            }
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Transparent)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}