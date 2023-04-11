package io.dongxi.application

import io.dongxi.view.BaseView
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
import io.nacular.doodle.utils.Resizer
import kotlinx.coroutines.*

class DynamicListsApp(
    display: Display,
    uiDispatcher: CoroutineDispatcher,
    animator: Animator,
    pathMetrics: PathMetrics,
    fonts: FontLoader,
    theme: DynamicTheme,
    themes: ThemeManager,
    images: ImageLoader,
    textMetrics: TextMetrics,
    linkStyler: NativeHyperLinkStyler,
    focusManager: FocusManager,
    popups: PopupManager,
    modals: ModalManager
) : Application {

    init {
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        appScope.launch(uiDispatcher) {
            val titleFont = fonts {
                size = 26; weight = 400; families = listOf("Helvetica Neue", "Helvetica", "Arial", "sans-serif")
            }!!
            val menuLinkFont = fonts {
                size = 24; weight = 300; families = listOf("Arial")
            }!!
            val listFont = fonts(titleFont) { size = 14 }!!
            val tabPanelFont = fonts(titleFont) { size = 12; weight = 400 }!!
            val footerFont = fonts(titleFont) { size = 10 }!!
            val config = DongxiConfig(
                listFont = listFont,
                menuLinkFont = menuLinkFont,
                titleFont = titleFont,
                tabPanelFont = tabPanelFont,
                footerFont = footerFont,
                filterFont = fonts(titleFont) { size = 14 }!!,
                boldFooterFont = fonts(footerFont) { weight = 400 }!!,
                placeHolderFont = fonts(listFont) { style = Font.Style.Italic }!!,
                checkForeground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23bddad5%22%20stroke-width%3D%223%22/%3E%3Cpath%20fill%3D%22%235dc2af%22%20d%3D%22M72%2025L42%2071%2027%2056l-4%204%2020%2020%2034-52z%22/%3E%3C/svg%3E")!!,
                checkBackground = images.load("data:image/svg+xml;utf8,%3Csvg%20xmlns%3D%22http%3A//www.w3.org/2000/svg%22%20width%3D%2240%22%20height%3D%2240%22%20viewBox%3D%22-10%20-18%20100%20135%22%3E%3Ccircle%20cx%3D%2250%22%20cy%3D%2250%22%20r%3D%2250%22%20fill%3D%22none%22%20stroke%3D%22%23ededed%22%20stroke-width%3D%223%22/%3E%3C/svg%3E")!!
            )

            themes.selected = theme

            val baseView = BaseView(
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
                modals
            ).apply {
                Resizer(this).apply { }
            }

            display += listOf(baseView)

            display.layout = constrain(baseView) { mainViewBounds ->
                mainViewBounds.edges eq parent.edges
            }

            display.fill(config.appBackground.paint)
        }
    }

    override fun shutdown() {
        TODO("Not implemented")
    }
}
