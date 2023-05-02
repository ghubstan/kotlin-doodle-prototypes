package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.dongxi.view.MenuEvent.SHOW_ANEIS
import io.dongxi.view.MenuEvent.SHOW_HOME
import io.dongxi.view.PageType.HOME
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.*
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance


@Suppress("unused")
class BaseView(config: DongxiConfig, commonDI: DI) : View() {

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

    private val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()

    private val mainScope = MainScope() // the scope of MainView class, uses Dispatchers.Main.

    private val menu = Menu(config, commonDI).apply {}

    private val pageFactory = PageFactory(config, commonDI)

    private var currentPage = pageFactory.buildPage(HOME) as View

    init {

        mainScope.launch {
            menuEventBus.events.filter { event ->
                event != null
            }.collectLatest {
                if (it == SHOW_HOME) {
                    println("Received SHOW_HOME  event.  TODO: display home page.")
                } else if (it == SHOW_ANEIS) {
                    println("Received SHOW_ANEIS event.  TODO: display rings page")
                }
            }
        }

        children += listOf(currentPage, menu)
        layout = constrain(currentPage, menu) { currentPageBounds, menuBounds ->

            currentPageBounds.edges eq parent.edges
            menuBounds.top eq 10
            menuBounds.right eq parent.right - 10
            menuBounds.left eq parent.right - 100
            menuBounds.height eq 100
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.White)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}