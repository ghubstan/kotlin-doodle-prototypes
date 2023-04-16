package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ICompleteRingContainer
import io.dongxi.model.ProductCategory.RING
import io.dongxi.model.Ring
import io.dongxi.model.RingStone
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoreMetadata.getLargeRingMetadata
import io.dongxi.util.ColorUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.*
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
import kotlinx.coroutines.async

class CenterPanel(
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
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Dimension.Width)
        foregroundColor = Color.Transparent
    }

    private val completeProductContainer = if (pageType.productCategory == RING) {
        getCompleteRingContainer()
    } else {
        getDummyBaseProductsContainer()
    }


    init {
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(tempLabel, completeProductContainer)
        layout = constrain(tempLabel, completeProductContainer) { tempLabelBounds, completeProductContainerBounds ->
            tempLabelBounds.left eq 5
            tempLabelBounds.top eq 10
            tempLabelBounds.bottom eq 26

            completeProductContainerBounds.top eq tempLabelBounds.bottom + 5
            completeProductContainerBounds.left eq tempLabelBounds.left
            completeProductContainerBounds.width eq parent.width
            completeProductContainerBounds.bottom eq parent.bottom - 5
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, ColorUtils.floralWhite())
    }

    override fun layoutForCurrentProductCategory() {
        // println("${panelInstanceName()} currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")

        tempLabel.text =
            "${currentBaseProduct.productCategory.name} ${currentBaseProduct.name ?: ""} with STONE ${currentAccessory.name ?: ""}"

        relayout()
    }

    override fun layoutForCurrentAccessorySelection() {
        println("${panelInstanceName()} currentAccessory: $currentAccessory")

        tempLabel.text =
            "${currentBaseProduct.productCategory.name} ${currentBaseProduct.name ?: ""} with STONE ${currentAccessory.name ?: ""}"

        relayout()
    }

    override fun layoutForCompletedJewel() {
        try {
            if (!currentBaseProduct.isSet()) {
                println("ERROR: ${panelInstanceName()} currentBaseProduct is not set: $currentBaseProduct")
            }
            val largeRingMetadata = getLargeRingMetadata(currentBaseProduct.name!!)
            val newRingName: String = largeRingMetadata.first
            val newRingFile: String = largeRingMetadata.second
            val newRingImage = mainScope.async { newRingFile.let { images.load(it) }!! }
            val newRing = Ring(newRingName, newRingFile, newRingImage)

            if (!currentAccessory.isSet()) {
                println("WARNING: ${panelInstanceName()} currentAccessory is not set: $currentAccessory")
                setDefaultAccessory()
                println(" ${panelInstanceName()} Default currentAccessory: $currentAccessory")
            }

            /*
            val newStoneName: String = currentAccessory.name.toString() // NPE?
            val newStoneFile: String = currentAccessory.file.toString() // NPE?
            val newStoneImage = mainScope.async { newStoneFile.let { images.load(it) }!! }
            val newStone = RingStone(newStoneName, newStoneFile, newStoneImage)
            */
            val newStone = RingStone(currentAccessory.name!!, currentAccessory.file!!, currentAccessory.image!!)


            try {
                if (completeProductContainer is ICompleteRingContainer) {
                    completeProductContainer.update(newRing, newStone)
                }
            } catch (ex: Exception) {
                println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
                println("Illegal Cast?")
            }

            completeProductContainer.relayout()
            relayout()
        } catch (ex: Exception) {
            println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
            println("${panelInstanceName()} currentAccessory = $currentAccessory")
        }
    }

}