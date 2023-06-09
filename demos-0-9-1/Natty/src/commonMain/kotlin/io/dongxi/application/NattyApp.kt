package io.dongxi.application

import io.dongxi.page.BaseView
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.application.Application
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Display
import io.nacular.doodle.drawing.Font
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.*
import org.kodein.di.DI
import org.kodein.di.instance

class NattyApp(commonDI: DI) : Application {

    private val animator: Animator by commonDI.instance<Animator>()
    private val display: Display by commonDI.instance<Display>()
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

    init {
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        appScope.launch(uiDispatcher) {
            val titleFont = fonts {
                size = 24; weight = 400; families = listOf("Helvetica Neue", "Helvetica", "Arial", "sans-serif")
            }!!
            val buttonFont = fonts {
                size = 12; weight = 100; families = listOf("Helvetica Neue", "Helvetica", "Arial", "sans-serif")
            }!!
            val listFont = fonts(titleFont) { size = 10 }!!
            val panelDebugFont = fonts(titleFont) { size = 8; weight = 100 }!!
            val footerFont = fonts(titleFont) { size = 10 }!!
            val formTextFieldFont = fonts(titleFont) { size = 12 }!!
            val formTextFieldDelimiterFont = fonts(titleFont) { size = 18; weight = 300; }!!
            val smallFont = fonts(titleFont) { size = 12; weight = 100; }!!
            val config = DongxiConfig(
                listFont = listFont,
                titleFont = titleFont,
                panelDebugFont = panelDebugFont,
                footerFont = footerFont,
                filterFont = fonts(titleFont) { size = 14 }!!,
                formTextFieldFont = formTextFieldFont,
                formTextFieldDelimiterFont = formTextFieldDelimiterFont,
                boldFooterFont = fonts(footerFont) { weight = 400 }!!,
                placeHolderFont = fonts(listFont) { style = Font.Style.Italic }!!,
                smallFont = smallFont,
                buttonFont = buttonFont,
                checkForeground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23bddad5%22%20stroke-width%3D%223%22/%3E%3Cpath%20fill%3D%22%235dc2af%22%20d%3D%22M72%2025L42%2071%2027%2056l-4%204%2020%2020%2034-52z%22/%3E%3C/svg%3E")!!,
                checkBackground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E")!!
            )

            themes.selected = theme

            val baseView = BaseView(config, commonDI).apply {}

            display += listOf(baseView)

            display.layout = constrain(baseView) { mainViewBounds ->
                mainViewBounds.edges eq parent.edges
            }

            display.fill(config.appBackgroundColor.paint)
        }
    }

    override fun shutdown() {
        TODO("Not implemented")
    }
}
