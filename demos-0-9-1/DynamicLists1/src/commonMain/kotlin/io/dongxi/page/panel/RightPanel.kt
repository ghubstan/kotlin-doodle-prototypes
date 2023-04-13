package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.RING
import io.dongxi.model.RingStonesContainer
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.Color.Companion.Transparent
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

class RightPanel(
    pageType: PageType,
    config: DongxiConfig,
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
    modals: ModalManager,
    menuEventBus: MenuEventBus,
    baseProductSelectEventBus: BaseProductSelectEventBus,
    accessorySelectEventBus: AccessorySelectEventBus
) : AbstractPanel(
    pageType,
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
) {

    private val tempLabel = io.nacular.doodle.controls.text.Label(
        "NADA",
        Middle,
        Center
    ).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        foregroundColor = Transparent
    }

    private val accessoryListContainer = if (pageType.productCategory == RING) {
        getRingStonesContainer(/*  TODO Pass default ring to know which stone list to load */)
    } else {
        getDummyBaseProductsContainer()
    }

    init {
        size = Size(200, 200)
        children += listOf(tempLabel, accessoryListContainer)
        layout = constrain(tempLabel, accessoryListContainer) { tempLabelBounds, accessoryListContainerBounds ->
            tempLabelBounds.left eq 5
            tempLabelBounds.top eq 10
            tempLabelBounds.bottom eq 26

            accessoryListContainerBounds.top eq tempLabelBounds.bottom + 5
            accessoryListContainerBounds.left eq tempLabelBounds.left
            accessoryListContainerBounds.width eq parent.width
            accessoryListContainerBounds.bottom eq parent.bottom - 5
        }
    }

    override fun layoutForCurrentProductCategory() {
        println("RightPanel currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("RightPanel currentBaseProduct: $currentBaseProduct")

        tempLabel.text =
            "${currentBaseProduct.productCategory.name} ${currentBaseProduct.name ?: ""} ${currentBaseProduct.file ?: ""}"


        (accessoryListContainer as RingStonesContainer).clearModel()
        accessoryListContainer.loadModel(currentBaseProduct.name ?: "A")
        accessoryListContainer.relayout()

        relayout()
    }


    override fun layoutForCurrentAccessorySelection() {
        println("RightPanel currentAccessory: $currentAccessory")
        relayout()
    }
}
