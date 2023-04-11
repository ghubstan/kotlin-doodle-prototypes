package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
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

class EarRingsPage(
    override val config: DongxiConfig,
    override val uiDispatcher: CoroutineDispatcher,
    override val animator: Animator,
    override val pathMetrics: PathMetrics,
    override val fonts: FontLoader,
    override val theme: DynamicTheme,
    override val themes: ThemeManager,
    override val images: ImageLoader,
    override val textMetrics: TextMetrics,
    override val linkStyler: NativeHyperLinkStyler,
    override val focusManager: FocusManager,
    override val popups: PopupManager,
    override val modals: ModalManager
) : IPage, View() {

    private val mainScope = MainScope() // the scope of EarRingsPage class, uses Dispatchers.Main.

    private val pageTitle = Label("Brincos", Middle, Center).apply {
        height = 26.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(text, config.titleFont, Black.paint)
    }

    init {
        children += listOf(pageTitle)
        layout = constrain(pageTitle) { titleBounds ->
            titleBounds.top eq 10
            titleBounds.centerX eq parent.centerX
            titleBounds.height eq 30
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.White)
    }

    override fun description(): String {
        return pageTitle.text
    }

    override fun shutdown() {
        TODO("Not implemented")
    }
}