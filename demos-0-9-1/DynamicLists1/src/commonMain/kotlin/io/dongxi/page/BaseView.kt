package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.PageType.*
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
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
        linkStyler,
        focusManager,
        popups,
        modals,
        menuEventBus,
        baseProductSelectEventBus,
        accessorySelectEventBus
    )

    private var currentPage = pageFactory.buildPage(HOME) as View
    // private var currentPage = pageFactory.buildPage(RINGS) as View

    init {

        mainScope.launch {
            menuEventBus.events.filterNotNull().collectLatest {

                println("Received ${it.name} event")

                when (it) {
                    GO_HOME -> {
                        currentPage = pageFactory.buildPage((HOME)) as View
                    }

                    GO_BRACELETS -> {
                        currentPage = pageFactory.buildPage((BRACELETS)) as View
                    }

                    GO_EARRINGS -> {
                        currentPage = pageFactory.buildPage((EAR_RINGS)) as View
                    }

                    GO_NECKLACES -> {
                        currentPage = pageFactory.buildPage((NECKLACES)) as View
                    }

                    GO_RINGS -> {
                        currentPage = pageFactory.buildPage((RINGS)) as View
                    }

                    GO_SCAPULARS -> {
                        currentPage = pageFactory.buildPage((SCAPULARS)) as View
                    }

                    GO_ABOUT -> {
                        currentPage = pageFactory.buildPage((ABOUT)) as View
                    }

                    LOGOUT -> {
                        println("Received LOGOUT event.  TODO: logout")
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