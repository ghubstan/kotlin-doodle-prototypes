package io.dongxi.application

import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel

class ClickMeView(
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
    private val modals: ModalManager
) : View() {

    private val mainScope = MainScope() // the scope of ClickMeView class, uses Dispatchers.Main.

    private val menuIcon: LazyPhoto =
        LazyPhoto(mainScope.async { images.load("drawer-menu.svg")!! }).apply {
            // Need a size or it will not render.
            size = Size(65, 65)
        }

    private val label = Label("Click Me", Middle, Center).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(this.text, config.titleFont, Black.paint)
        foregroundColor = Color.Cyan
    }

    init {
        size = Size(300, 300)
        children += listOf(label, menuIcon)
        layout = constrain(label, menuIcon) { labelBounds, menuIconBounds ->
            labelBounds.top eq 20
            labelBounds.centerX eq parent.centerX
            labelBounds.width.preserve
            labelBounds.bottom eq labelBounds.top + label.height

            menuIconBounds.top eq labelBounds.bottom + 2
            menuIconBounds.centerX eq labelBounds.centerX
            menuIconBounds.width eq 65
            menuIconBounds.height.preserve
        }
        pointerChanged += clicked {
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.Lightgray)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}