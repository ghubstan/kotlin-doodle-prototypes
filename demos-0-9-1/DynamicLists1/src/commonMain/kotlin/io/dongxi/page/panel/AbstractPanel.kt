package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory
import io.dongxi.model.ProductCategory.*
import io.dongxi.model.SelectedBaseProduct
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

abstract class AbstractPanel(
    val config: DongxiConfig,
    val uiDispatcher: CoroutineDispatcher,
    val animator: Animator,
    val pathMetrics: PathMetrics,
    val fonts: FontLoader,
    val theme: DynamicTheme,
    val themes: ThemeManager,
    val images: ImageLoader,
    val textMetrics: TextMetrics,
    val linkStyler: NativeHyperLinkStyler,
    val focusManager: FocusManager,
    val popups: PopupManager,
    val modals: ModalManager,
    val menuEventBus: MenuEventBus,
    val baseProductSelectEventBus: BaseProductSelectEventBus
) : IPanel, View() {

    val mainScope = MainScope() // The scope of Panel class (and subclasses), uses Dispatchers.Main.

    lateinit var currentProductCategory: ProductCategory
    lateinit var currentBaseProduct: SelectedBaseProduct

    init {
        mainScope.launch {
            menuEventBus.events.filterNotNull().collectLatest {

                println("Received ${it.name} event")

                when (it) {
                    GO_HOME -> {
                        // Nothing happens
                    }

                    GO_BRACELETS -> {
                        currentProductCategory = BRACELET
                    }

                    GO_EARRINGS -> {
                        currentProductCategory = EARRING
                    }

                    GO_NECKLACES -> {
                        currentProductCategory = NECKLACE
                    }

                    GO_RINGS -> {
                        currentProductCategory = RING
                    }

                    GO_SCAPULARS -> {
                        currentProductCategory = SCAPULAR
                    }

                    GO_ABOUT -> {
                        // Nothing happens
                    }

                    LOGOUT -> {
                        // Nothing happens
                    }
                }

                // Now update the view
                println("Current ProductCategory: $currentProductCategory")
                // updateView(currentPage)
            }
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Lightgray)
    }

    // Destroys an instance of RingsWidget.
    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    // Helper to use constrain with 6 items
    operator fun <T> List<T>.component6() = this[5]

    // Helper to use constrain with 7 items
    operator fun <T> List<T>.component7() = this[6]

    // Helper to use constrain with 8 items
    operator fun <T> List<T>.component8() = this[7]

    // Helper to use constrain with 9 items
    operator fun <T> List<T>.component9() = this[8]

    // Helper to use constrain with 10 items
    operator fun <T> List<T>.component10() = this[9]

    // Helper to use constrain with 11 items
    operator fun <T> List<T>.component11() = this[10]

    // Helper to use constrain with 12 items
    operator fun <T> List<T>.component12() = this[11]
}