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
import org.kodein.di.DI
import org.kodein.di.instance

@Suppress("unused")
class HomePage(private val config: DongxiConfig, commonDI: DI) : IPage, View() {

    private val animator: Animator by commonDI.instance<Animator>()
    private val focusManager: FocusManager by commonDI.instance<FocusManager>()
    private val fonts: FontLoader by commonDI.instance<FontLoader>()
    private val images: ImageLoader by commonDI.instance<ImageLoader>()
    private val linkStyler: NativeHyperLinkStyler by commonDI.instance<NativeHyperLinkStyler>()
    private val modals: ModalManager by commonDI.instance<ModalManager>()
    private val pathMetrics: PathMetrics by commonDI.instance<PathMetrics>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()
    private val theme: DynamicTheme by commonDI.instance<DynamicTheme>()
    private val themes: ThemeManager by commonDI.instance<ThemeManager>()
    private val uiDispatcher: CoroutineDispatcher by commonDI.instance<CoroutineDispatcher>()

    private val mainScope = MainScope() // the scope of HomePage class, uses Dispatchers.Main.

    private val pageTitle = Label("Home", Middle, Center).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(text, config.titleFont, Black.paint)
        foregroundColor = Color.Cyan
    }

    init {
        children += listOf(pageTitle)
        layout = constrain(pageTitle) { titleBounds ->
            titleBounds.top eq 10
            titleBounds.centerX eq parent.centerX
            titleBounds.height eq 24
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.White)
    }

    override fun description(): String {
        TODO("Not implemented")
    }

    override fun shutdown() {
        TODO("Not implemented")
    }
}