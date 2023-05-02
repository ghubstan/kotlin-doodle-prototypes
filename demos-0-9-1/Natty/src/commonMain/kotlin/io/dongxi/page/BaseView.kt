package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.PageType.*
import io.dongxi.util.ClassUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color.Companion.White
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.ConstraintLayout
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance


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

    private val pageFactory = PageFactory(config, commonDI)

    private val mainScope = MainScope()

    // private var currentPage = pageFactory.buildPage(HOME) as View
    private var currentPage = pageFactory.buildPage(RINGS) as View
    // private var currentPage = pageFactory.buildPage(NECKLACES) as View

    init {

        mainScope.launch {
            menuEventBus.events.filterNotNull().collectLatest {

                println("${ClassUtils.simpleClassName(this)} Received ${it.name} event")

                // TODO Create a mapping of event -> page-type to reduce this to a one-liner?
                
                when (it) {
                    GO_HOME -> currentPage = pageFactory.buildPage((HOME)) as View
                    GO_BRACELETS -> currentPage = pageFactory.buildPage((BRACELETS)) as View
                    GO_EARRINGS -> currentPage = pageFactory.buildPage((EAR_RINGS)) as View
                    GO_NECKLACES -> currentPage = pageFactory.buildPage((NECKLACES)) as View
                    GO_RINGS -> currentPage = pageFactory.buildPage((RINGS)) as View
                    GO_SCAPULARS -> currentPage = pageFactory.buildPage((SCAPULARS)) as View
                    GO_ABOUT -> currentPage = pageFactory.buildPage((ABOUT)) as View
                    GO_LOGIN -> currentPage = pageFactory.buildPage((LOGIN)) as View
                    GO_LOGOUT -> currentPage = pageFactory.buildPage((LOGOUT)) as View
                    GO_BASKET -> currentPage = pageFactory.buildPage((BASKET)) as View
                    GO_PAYMENT -> currentPage = pageFactory.buildPage((PAYMENT)) as View
                    GO_REGISTER -> currentPage = pageFactory.buildPage((REGISTER)) as View
                }
                // Now update the view.
                updateView(currentPage)
            }
        }

        children += currentPage
        layout = constrain(currentPage, fill)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, White)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    //  Add and remove constraint for the IPage view dynamically.
    private fun updateView(new: View) {
        val oldView = children.firstOrNull()

        when (oldView) {
            null -> children += new
            else -> children[0] = new
        }

        // Only do this if you want to continue using constraint layout
        (layout as ConstraintLayout).let { layout ->
            oldView?.let {
                layout.unconstrain(it, fill)
            }

            layout.constrain(new, fill)
        }
    }
}