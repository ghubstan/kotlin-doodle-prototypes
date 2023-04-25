package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.PageType.*
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
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
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class BaseView(
    private val config: DongxiConfig,
    private val uiDispatcher: CoroutineDispatcher,
    private val animator: Animator,
    private val pathMetrics: PathMetrics,
    private val fonts: FontLoader,
    private val theme: DynamicTheme,
    private val themes: ThemeManager,
    private val images: ImageLoader,
    private val textMetrics: TextMetrics,
    private val textFieldStyler: NativeTextFieldStyler,
    private val linkStyler: NativeHyperLinkStyler,
    private val focusManager: FocusManager,
    private val popups: PopupManager,
    private val modals: ModalManager
) : View() {

    private val mainScope = MainScope() // the scope of MainView class, uses Dispatchers.Main.

    private val menuEventBus = MenuEventBus()
    private val baseProductSelectEventBus = BaseProductSelectEventBus()
    private val accessorySelectEventBus = AccessorySelectEventBus()

    private val pageFactory = PageFactory(
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
        menuEventBus,
        baseProductSelectEventBus,
        accessorySelectEventBus
    )

    // private var currentPage = pageFactory.buildPage(HOME) as View
    // private var currentPage = pageFactory.buildPage(LOGIN) as View
    private var currentPage = pageFactory.buildPage(REGISTER) as View

    init {

        mainScope.launch {
            menuEventBus.events.filterNotNull().collectLatest {

                println("${ClassUtils.simpleClassName(this)} Received ${it.name} event")

                when (it) {
                    GO_HOME -> {
                        currentPage = pageFactory.buildPage((HOME)) as View
                    }

                    GO_REGISTER -> {
                        currentPage = pageFactory.buildPage((REGISTER)) as View
                    }

                    GO_LOGIN -> {
                        currentPage = pageFactory.buildPage((LOGIN)) as View
                    }

                    GO_BASKET -> {
                        currentPage = pageFactory.buildPage((BASKET)) as View
                    }

                    GO_PAYMENT -> {
                        currentPage = pageFactory.buildPage((PAYMENT)) as View
                    }

                    GO_LOGOUT -> {
                        currentPage = pageFactory.buildPage((LOGOUT)) as View
                    }
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