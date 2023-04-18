package io.dongxi.model


import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.*
import io.dongxi.model.ScaledImage.*
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEvent.*
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata
import io.dongxi.storage.PendantStoreMetadata
import io.dongxi.storage.RingStoneStoreMetadata
import io.dongxi.storage.RingStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.*

@Suppress("unused")
abstract class AbstractCompleteProductContainer(
    final override val pageType: PageType,
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
    private val baseProductSelectEventBus: BaseProductSelectEventBus,
    private val accessorySelectEventBus: AccessorySelectEventBus
) : ICompleteProductContainer, Container() {

    override val mainScope = MainScope()

    val debugLabel = Label("Nenhum", Middle, Center).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    private val defaultProductMetadata = getDefaultProductMetadata()
    private val defaultProductName = defaultProductMetadata.first
    private val defaultProductFile = defaultProductMetadata.second

    private val defaultAccessoryMetadata = getDefaultAccessoryMetadata()
    private val defaultAccessoryName = defaultAccessoryMetadata.first
    private val defaultAccessoryFile = defaultAccessoryMetadata.second

    private fun getDefaultProductMetadata(): Pair<String, String> {
        return when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> NecklaceStoreMetadata.getLargeNecklaceMetadata("A")
            RING -> RingStoreMetadata.getLargeRingMetadata("A")
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    }

    private fun getDefaultAccessoryMetadata(): Pair<String, String> {
        return when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> PendantStoreMetadata.getPendants(defaultProductMetadata.first)[0]
            RING -> RingStoneStoreMetadata.getStones(defaultProductMetadata.first)[0]
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    }

    var product: IProduct = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()

        NECKLACE -> Necklace(
            defaultProductName,
            defaultProductFile,
            mainScope.async { images.load(defaultProductFile)!! })

        RING -> Ring(
            defaultProductName,
            defaultProductFile,
            mainScope.async { images.load(defaultProductFile)!! })

        SCAPULAR -> TODO()
        NONE -> TODO()
    }

    var accessory: IProductAccessory = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()

        NECKLACE -> NecklacePendant(
            defaultAccessoryName,
            defaultAccessoryFile,
            mainScope.async { images.load(defaultAccessoryFile)!! })

        RING -> RingStone(
            defaultAccessoryName,
            defaultAccessoryFile,
            mainScope.async { images.load(defaultAccessoryFile)!! })

        SCAPULAR -> TODO()
        NONE -> TODO()
    }

    val productPhoto = LazyImage(
        pendingImage = product.image,
        canvasDestination = when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> LARGE_NECKLACE.canvasDestination
            RING -> LARGE_RING.canvasDestination
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    )

    val accessoryPhoto = LazyImage(
        pendingImage = accessory.image,
        canvasDestination = when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> LARGE_NECKLACE_PENDANT.canvasDestination
            RING -> LARGE_RING_STONE.canvasDestination
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    )

    override fun update(product: IProduct, accessory: IProductAccessory) {
        // Reconfigure the view to represent the new product and accessory installed in it.
        this.product = product
        this.accessory = accessory

        productPhoto.pendingImage = product.image
        accessoryPhoto.pendingImage = accessory.image

        updateDebugLabelText(product, accessory)
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    fun updateDebugLabelText(product: IProduct, accessory: IProductAccessory) {
        debugLabel.text = "Name:  ${product.name}  Accessory:  ${accessory.name}"
    }
}