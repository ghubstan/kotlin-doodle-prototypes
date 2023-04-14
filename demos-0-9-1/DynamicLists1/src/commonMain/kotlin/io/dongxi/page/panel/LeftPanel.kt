package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.RING
import io.dongxi.model.SelectedBaseProduct
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoreMetadata
import io.dongxi.util.ColorUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async

class LeftPanel(
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
    private val tempLabel = Label(
        "NADA",
        Middle,
        Center
    ).apply {
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    private val baseProductListContainer = if (pageType.productCategory == RING) {
        getBaseRingsContainer()
    } else {
        getDummyBaseProductsContainer()
    }

    init {
        clipCanvasToBounds = false
        size = Size(200, 200)

        // Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack Hack
        if (pageType.productCategory == RING) {
            if (currentBaseProduct.name == null) {
                println("LeftPanel has NULL $currentBaseProduct  WTF!")

                // TODO Fix design bug:  What is selected product (size)?  Which is it, LARGE or small?
                // val defaultRingMetadata = RingStoreMetadata.getLargeRingMetadata("A")
                val defaultRingMetadata = RingStoreMetadata.getSmallRingMetadata("A")

                currentBaseProduct = SelectedBaseProduct(
                    currentProductCategory,
                    defaultRingMetadata.first,
                    defaultRingMetadata.second,
                    mainScope.async { images.load(defaultRingMetadata.second)!! })
            }

            tempLabel.text =
                "${currentBaseProduct.productCategory.name} ${currentBaseProduct.name ?: ""} ${currentBaseProduct.file ?: ""}"
        }

        children += listOf(tempLabel, baseProductListContainer)
        layout = constrain(tempLabel, baseProductListContainer) { tempLabelBounds, baseRingsContainerBounds ->
            tempLabelBounds.left eq 5
            tempLabelBounds.top eq 10
            tempLabelBounds.bottom eq 26

            baseRingsContainerBounds.top eq tempLabelBounds.bottom + 5
            baseRingsContainerBounds.left eq tempLabelBounds.left
            baseRingsContainerBounds.width eq parent.width
            baseRingsContainerBounds.bottom eq parent.bottom - 5
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, ColorUtils.floralWhite())
    }

    override fun layoutForCurrentProductCategory() {
        // println("LeftPanel currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("LeftPanel currentBaseProduct: $currentBaseProduct")

        tempLabel.text =
            "${currentBaseProduct.productCategory.name} ${currentBaseProduct.name ?: ""} ${currentBaseProduct.file ?: ""}"

        relayout()
    }

    override fun layoutForCurrentAccessorySelection() {
        println("LeftPanel currentAccessory: $currentAccessory")
        relayout()
    }

    override fun layoutForCompletedJewel() {
        // noop
    }
}
