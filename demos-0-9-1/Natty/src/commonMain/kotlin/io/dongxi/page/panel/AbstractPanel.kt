package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.*
import io.dongxi.model.AccessoryCategory.PENDANT
import io.dongxi.model.AccessoryCategory.STONE
import io.dongxi.model.ProductCategory.*
import io.dongxi.page.MenuEvent.*
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.FormControlFactory
import io.dongxi.storage.NecklaceStoreMetadata
import io.dongxi.storage.PendantStoreMetadata.getPendants
import io.dongxi.storage.RingStoneStoreMetadata.getStones
import io.dongxi.storage.RingStoreMetadata
import io.dongxi.util.ColorUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import org.kodein.di.DI
import org.kodein.di.instance

abstract class AbstractPanel(
    override val pageType: PageType,
    val config: DongxiConfig,
    val commonDI: DI,
    val formControlFactory: FormControlFactory
) : IPanel, Container() {

    val animator: Animator by commonDI.instance<Animator>()
    val focusManager: FocusManager by commonDI.instance<FocusManager>()
    val fonts: FontLoader by commonDI.instance<FontLoader>()
    val images: ImageLoader by commonDI.instance<ImageLoader>()
    val linkStyler: NativeHyperLinkStyler by commonDI.instance<NativeHyperLinkStyler>()
    val modals: ModalManager by commonDI.instance<ModalManager>()
    val pathMetrics: PathMetrics by commonDI.instance<PathMetrics>()
    val popups: PopupManager by commonDI.instance<PopupManager>()
    val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()
    val theme: DynamicTheme by commonDI.instance<DynamicTheme>()
    val themes: ThemeManager by commonDI.instance<ThemeManager>()
    val uiDispatcher: CoroutineDispatcher by commonDI.instance<CoroutineDispatcher>()

    val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    val baseProductSelectEventBus: BaseProductSelectEventBus by commonDI.instance<BaseProductSelectEventBus>()
    val accessorySelectEventBus: AccessorySelectEventBus by commonDI.instance<AccessorySelectEventBus>()

    val mainScope = MainScope() // The scope of AbstractPanel class (and subclasses), uses Dispatchers.Main.

    var currentProductCategory: ProductCategory = pageType.productCategory
    var currentBaseProduct: SelectedBaseProduct = SelectedBaseProduct(
        productCategory = currentProductCategory,
        name = null,
        file = null,
        image = null
    )
    var currentAccessory: SelectedAccessory = SelectedAccessory(
        accessoryCategory = AccessoryCategory.NONE,
        name = null,
        file = null,
        image = null
    )

    init {
        mainScope.launch {
            menuEventBus.events.filterNotNull().collectLatest {
                println("${panelInstanceName()} Received ${it.name} event")

                currentProductCategory = when (it) {
                    GO_HOME -> NONE
                    GO_BRACELETS -> BRACELET
                    GO_EARRINGS -> EARRING
                    GO_NECKLACES -> NECKLACE
                    GO_RINGS -> RING
                    GO_SCAPULARS -> SCAPULAR
                    GO_ABOUT -> NONE
                    GO_BASKET -> NONE
                    GO_LOGIN -> NONE
                    GO_LOGOUT -> NONE
                    GO_PAYMENT -> NONE
                    GO_REGISTER -> NONE
                }

                // println("${panelInstanceName()} current ProductCategory: $currentProductCategory")
                // Now update the panel with the current product category, if there is one.
                layoutForCurrentProductCategory()
            }
        }

        mainScope.launch {
            try {
                baseProductSelectEventBus.events.filterNotNull().collectLatest {
                    currentBaseProduct = it.baseProductDetail()

                    setDefaultAccessory()

                    layoutForCurrentBaseProductSelection()
                    layoutForCompletedJewel()
                }
            } catch (ex: Exception) {
                println("EXCEPTION ${panelInstanceName()} -> baseProductSelectEventBus.events.filterNotNull():  ${ex.stackTraceToString()}")
                println("${panelInstanceName()}.currentBaseProduct.isSet = ${currentBaseProduct.isSet()}")
                println("${panelInstanceName()}.currentAccessory.isSet = ${currentAccessory.isSet()}")
            }
        }

        mainScope.launch {
            try {
                accessorySelectEventBus.events.filterNotNull().collectLatest {
                    currentAccessory = it.accessoryDetail()

                    if (!currentBaseProduct.isSet()) {
                        setDefaultBaseProduct()
                    }

                    layoutForCurrentAccessorySelection()
                    layoutForCompletedJewel()
                }
            } catch (ex: Exception) {
                println("EXCEPTION ${panelInstanceName()} -> accessorySelectEventBus.events.filterNotNull():  ${ex.stackTraceToString()}")
                println("${panelInstanceName()}.currentBaseProduct.isSet = ${currentBaseProduct.isSet()}")
                println("${panelInstanceName()}.currentAccessory.isSet = ${currentAccessory.isSet()}")
            }
        }
    }

    abstract fun layoutForCurrentProductCategory()
    abstract fun layoutForCurrentBaseProductSelection()
    abstract fun layoutForCurrentAccessorySelection()
    abstract fun layoutForCompletedJewel()

    fun getDummyBaseProductsContainer(): Container {
        return DummyBaseProductsContainer(config, commonDI)
    }

    fun getBaseProductListContainer(): Container {
        return BaseProductListContainer(pageType, config, commonDI)
    }

    fun getProductAccessoryListContainer(): Container {
        return ProductAccessoryListContainer(pageType, config, commonDI)
    }

    fun getCompleteNecklaceContainer(): Container {
        return CompleteNecklaceContainer(pageType, config, commonDI)
    }

    fun getCompleteRingContainer(): Container {
        return CompleteRingContainer(pageType, config, commonDI)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, ColorUtils.ghostWhite())
    }

    // Destroys an instance of RingsWidget.
    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    fun setDefaultBaseProduct() {
        if (currentProductCategory == NONE) {
            println("AbstractPanel::${panelInstanceName()} cannot set default base product for product category NONE.")
            return
        } else {
            currentBaseProduct = getDefaultSelectedBaseProduct()
        }
    }

    fun setDefaultAccessory() {
        if (currentProductCategory == NONE) {
            println("AbstractPanel::${panelInstanceName()} cannot set default accessory for product category NONE.")
            return
        } else {
            currentAccessory = getDefaultAccessoryForBaseProduct()
        }
    }

    private fun getDefaultSelectedBaseProduct(): SelectedBaseProduct {

        return when (currentProductCategory) {

            NECKLACE -> {
                println("${panelInstanceName()} -> set default ${pageType.productCategory}")
                // A base necklace image is a small image.  Complete necklaces are large images.
                val defaultNecklaceMetadata = NecklaceStoreMetadata.getSmallNecklaceMetadata("A")
                val necklaceName = defaultNecklaceMetadata.first
                val necklaceFile = defaultNecklaceMetadata.second
                val necklaceImage = mainScope.async { images.load(necklaceFile)!! }
                SelectedBaseProduct(currentProductCategory, necklaceName, necklaceFile, necklaceImage)
            }

            RING -> {
                println("${panelInstanceName()} -> set default ${pageType.productCategory}")
                // A base ring image is a small image.  Complete rings are large images.
                val defaultRingMetadata = RingStoreMetadata.getSmallRingMetadata("A")
                val ringName = defaultRingMetadata.first
                val ringFile = defaultRingMetadata.second
                val ringImage = mainScope.async { images.load(ringFile)!! }
                SelectedBaseProduct(currentProductCategory, ringName, ringFile, ringImage)
            }

            else -> {
                // TODO
                println("AbstractPanel::${panelInstanceName()} -> TODO set default base product for product category $currentProductCategory")
                SelectedBaseProduct(NONE, null, null, null)
            }
        }
    }

    private fun getDefaultAccessoryForBaseProduct(): SelectedAccessory {

        return when (currentProductCategory) {

            // TODO Refactor out all duplicated code.

            NECKLACE -> {
                val necklaceName = currentBaseProduct.name!!
                // The default accessory is the one at the top of the accessory list.
                val defaultNecklaceMetadata: Pair<String, String> = getPendants(necklaceName)[0]
                val pendantName = defaultNecklaceMetadata.first
                val pendantFile = defaultNecklaceMetadata.second
                val pendantImage = mainScope.async { images.load(pendantFile)!! }
                SelectedAccessory(PENDANT, pendantName, pendantFile, pendantImage)
            }

            RING -> {
                val ringName = currentBaseProduct.name!!
                // The default accessory is the one at the top of the accessory list.
                val defaultStoneMetadata: Pair<String, String> = getStones(ringName)[0]
                val stoneName = defaultStoneMetadata.first
                val stoneFile = defaultStoneMetadata.second
                val stoneImage = mainScope.async { images.load(stoneFile)!! }
                SelectedAccessory(STONE, stoneName, stoneFile, stoneImage)
            }

            else -> {
                println("AbstractPanel::${panelInstanceName()} -> TODO set default accessory for product category category $currentProductCategory")
                return SelectedAccessory(AccessoryCategory.NONE, null, null, null)
            }
        }
    }

    fun panelInstanceName(): String {
        return when (this) {
            is TopPanel -> {
                "TopPanel"
            }

            is LeftPanel -> {
                "LeftPanel"
            }

            is CenterPanel -> {
                "CenterPanel"
            }

            is RightPanel -> {
                "RightPanel"
            }

            is FooterPanel -> {
                "FooterPanel"
            }

            is BaseContainer -> {
                "BaseContainer"
            }

            else -> {
                "UnknownPanel"
            }
        }
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