package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.*
import io.dongxi.model.ScaledImage.*
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata
import io.dongxi.storage.PendantStoreMetadata
import io.dongxi.storage.RingStoneStoreMetadata
import io.dongxi.storage.RingStoreMetadata
import io.dongxi.util.StringUtils.accessoryLabelText
import io.dongxi.util.StringUtils.productLabelText
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel

abstract class AbstractCompleteProductContainer(
    override val pageType: PageType,
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
) : ICompleteProductContainer, Container() {

    override val mainScope = MainScope()

    var product: IProduct = defaultProduct()
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

    var accessory: IProductAccessory = defaultProductAccessory(product)
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

    val accessoryPhotoLeftBounds: Int = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()
        NECKLACE -> 83
        RING -> 50
        SCAPULAR -> TODO()
        NONE -> TODO()
    }

    val accessoryPhotoCenterYBounds: Int = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()
        NECKLACE -> 217
        RING -> 122
        SCAPULAR -> TODO()
        NONE -> TODO()
    }


    val debugLabel = Label("Nenhum", Middle, Center).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    fun updateDebugLabelText() {
        debugLabel.text = productLabelText(product) + " " + accessoryLabelText(accessory)
    }

    override fun update(product: IProduct, accessory: IProductAccessory) {
        // Reconfigure the view to represent the new necklace-pendant installed in it.
        this.product = product
        this.accessory = accessory

        productPhoto.pendingImage = product.image
        accessoryPhoto.pendingImage = accessory.image

        updateDebugLabelText()
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    private fun defaultProduct(): IProduct {
        val name = defaultProductName()
        return when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> defaultNecklace(name)
            RING -> defaultRing(name)
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    }

    private fun defaultProductName(): String {
        return when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> "A"
            RING -> "A"
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    }

    private fun defaultProductAccessory(product: IProduct): IProductAccessory {
        return when (product.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> defaultNecklacePendant(product)
            RING -> defaultRingStone(product)
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    }

    private fun defaultNecklace(name: String): IProduct {
        val nameFileTuple = NecklaceStoreMetadata.getLargeNecklaceMetadata(name)
        return Necklace(
            nameFileTuple.first,
            nameFileTuple.second,
            mainScope.async { images.load(nameFileTuple.second)!! })
    }

    private fun defaultNecklacePendant(product: IProduct): IProductAccessory {
        val nameFileTuple: Pair<String, String> = PendantStoreMetadata.getPendants(product.name)[0]
        return NecklacePendant(
            nameFileTuple.first,
            nameFileTuple.second,
            mainScope.async { images.load(nameFileTuple.second)!! })
    }

    private fun defaultRing(name: String): IProduct {
        val nameFileTuple = RingStoreMetadata.getLargeRingMetadata(name)
        return Ring(nameFileTuple.first, nameFileTuple.second, mainScope.async { images.load(nameFileTuple.second)!! })
    }

    private fun defaultRingStone(product: IProduct): IProductAccessory {
        val nameFileTuple: Pair<String, String> = RingStoneStoreMetadata.getStones(product.name)[0]
        return RingStone(
            nameFileTuple.first,
            nameFileTuple.second,
            mainScope.async { images.load(nameFileTuple.second)!! })
    }
}