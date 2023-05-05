package io.dongxi.page.panel.menu

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.instance

@OptIn(ExperimentalCoroutinesApi::class)
class Menu(val config: DongxiConfig, commonDI: DI) : View() {

    private val images: ImageLoader by commonDI.instance<ImageLoader>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val mainScope = MainScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuIcon = LazyPhoto(mainScope.async { images.load("assets/images/drawer-menu.svg")!! })

    private val menuPopup = MenuPopup(config, commonDI).apply {
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
            popups.show(menuPopup, relativeTo = this) { menuPopupBounds, _ ->
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