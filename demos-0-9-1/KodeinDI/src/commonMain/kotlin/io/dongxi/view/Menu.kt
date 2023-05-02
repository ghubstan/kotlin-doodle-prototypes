package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.Strength
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.instance

class Menu(config: DongxiConfig, commonDI: DI) : View() {

    private val images: ImageLoader by commonDI.instance<ImageLoader>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val mainScope = MainScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuIcon = LazyPhoto(mainScope.async { images.load("drawer-menu.svg")!! })

    private val menuPopup = MenuPopup(config, commonDI).apply {
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

                menuPopupBounds.top greaterEq 5
                menuPopupBounds.left greaterEq 5
                menuPopupBounds.right lessEq parent.right - 5
                menuPopupBounds.height eq parent.height * 0.20
                menuPopupBounds.width eq parent.width * 0.20
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