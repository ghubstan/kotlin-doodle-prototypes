package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


class DummyBaseProductsContainer(
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
    private val modals: ModalManager,
    private val menuEventBus: MenuEventBus,
    private val baseProductSelectEventBus: BaseProductSelectEventBus
) : IProductListContainer, Container() {

    private val mainScope = MainScope() // The scope of DummyBaseProductsContainer class, uses Dispatchers.Main.

    private val tempLabel = io.nacular.doodle.controls.text.Label(
        "Dummy List (TODO)",
        Middle,
        Center
    ).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        foregroundColor = Color.Transparent
    }


    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        children += listOf(tempLabel)
        layout = constrain(tempLabel) { tempLabelBounds ->
            tempLabelBounds.left eq 5
            tempLabelBounds.top eq 10
            tempLabelBounds.bottom eq 26
        }
    }

    override fun loadModel() {
        // noop
    }

    override fun clearModel() {
        // noop
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}
