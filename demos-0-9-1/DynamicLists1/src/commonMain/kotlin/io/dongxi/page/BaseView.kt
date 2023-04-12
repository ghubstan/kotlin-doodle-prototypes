package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.PageType.*
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
import kotlinx.coroutines.flow.filter
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

    private val eventBus = MenuEventBus()

    private val menu = Menu(
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
        eventBus
    ).apply {
    }

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
        modals
    )

    private var currentPage = pageFactory.buildPage(HOME) as View

    init {

        mainScope.launch {
            eventBus.events.filter { event ->
                event != null
            }.collectLatest {

                println("Received ${it.name} event")

                when (it) {
                    GO_HOME -> {
                        currentPage = pageFactory.buildPage((HOME)) as View
                    }

                    GO_RINGS -> {
                        currentPage = pageFactory.buildPage((RINGS)) as View
                    }

                    GO_NECKLACES -> {
                        currentPage = pageFactory.buildPage((NECKLACES)) as View
                    }

                    GO_SCAPULARS -> {
                        currentPage = pageFactory.buildPage((SCAPULARS)) as View
                    }

                    GO_BRACELETS -> {
                        currentPage = pageFactory.buildPage((BRACELETS)) as View
                    }

                    GO_EAR_RINGS -> {
                        currentPage = pageFactory.buildPage((EAR_RINGS)) as View
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

        children += listOf(currentPage, menu)
        layout = constrain(currentPage, menu) { currentPageBounds, menuBounds ->

            currentPageBounds.edges eq parent.edges

            menuBounds.top eq 5
            menuBounds.right eq parent.right - parent.right * 0.08
            menuBounds.left eq parent.right - parent.right * 0.20
            // menuBounds.right eq parent.right - 60
            // menuBounds.left eq parent.right - 160
            menuBounds.height eq 100
        }

        /*
        // Only constrain menu since you need to add/remove constraint for the other view dynamically.
        children += listOf(menu)
        layout = constrain(menu) { menuBounds ->
            menuBounds.top eq 10
            menuBounds.right eq parent.right - 10
            menuBounds.left eq parent.right - 100
            menuBounds.height eq 100
        }
        // When I try this, neither the home page nor menu are displayed, even after
        // calling updateView(currentPage) on the line above  children += listOf(menu)
        */
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