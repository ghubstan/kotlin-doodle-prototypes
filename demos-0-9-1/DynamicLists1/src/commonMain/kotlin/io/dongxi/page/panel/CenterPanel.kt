package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.*
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ProductCategory.RING
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata
import io.dongxi.storage.RingStoreMetadata.getLargeRingMetadata
import io.dongxi.util.ColorUtils.floralWhite
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
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
    /*
    private val completeProductContainerMap = mutableMapOf<ProductCategory, CompleteProductContainer>()
    private fun completeProductContainer(): Container {
        return if (completeProductContainerMap.contains(pageType.productCategory)) {
            completeProductContainerMap[pageType.productCategory]!!
        } else {
            val container: Container = when (pageType.productCategory) {
                NECKLACE -> getCompleteProductContainer()
                RING -> getCompleteProductContainer()
                else -> getDummyBaseProductsContainer()
            }
            completeProductContainerMap[pageType.productCategory] = container as CompleteProductContainer
            completeProductContainerMap[pageType.productCategory]!!
        }
    }
     */


    private val completeProductContainer = when (pageType.productCategory) {
        NECKLACE -> getCompleteNecklaceContainer()
        RING -> getCompleteRingContainer()

        // TODO Do not delete until I find out why using this fails to consistently
        //      update the center panel's complete product images.
        // NECKLACE -> getCompleteProductContainer()
        // RING -> getCompleteProductContainer()

        else -> getDummyBaseProductsContainer()
    }

    init {
        println("CENTER PANEL -> INIT -> PAGE TYPE? $pageType")
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(completeProductContainer)
        layout = constrain(completeProductContainer, fill)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, floralWhite())
    }

    override fun layoutForCurrentProductCategory() {
        println("${panelInstanceName()} layoutForCurrentProductCategory -> currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")
        relayout()
    }

    override fun layoutForCurrentAccessorySelection() {
        println("${panelInstanceName()} layoutForCurrentBaseProductSelection -> currentAccessory: $currentAccessory")
        relayout()
    }

    override fun layoutForCompletedJewel() {
        println("${panelInstanceName()} layoutForCompletedJewel -> currentProductCategory: $currentProductCategory")

        try {
            if (!currentBaseProduct.isSet()) {
                println("ERROR: ${panelInstanceName()} currentBaseProduct is not set: $currentBaseProduct")
            }

            if (!currentAccessory.isSet()) {
                println("WARNING: ${panelInstanceName()} currentAccessory is not set: $currentAccessory")
                setDefaultAccessory()
                println(" ${panelInstanceName()} Default currentAccessory: $currentAccessory")
            }

            when (currentProductCategory) {

                // TODO Refactor out duplicated code.

                NECKLACE -> {
                    val newNecklace = getLargeNecklace()
                    val newPendant =
                        NecklacePendant(currentAccessory.name!!, currentAccessory.file!!, currentAccessory.image!!)

                    try {
                        // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
                        if (completeProductContainer is CompleteNecklaceContainer) {
                            println("${panelInstanceName()} -> Call ICompleteProductContainer.update(newNecklace, newPendant)")
                            completeProductContainer.update(newNecklace, newPendant)
                        }

                    } catch (ex: Exception) {
                        println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
                        println("Illegal Cast?")
                    }
                }

                RING -> {
                    val newRing = getLargeRing()
                    val newStone = RingStone(currentAccessory.name!!, currentAccessory.file!!, currentAccessory.image!!)

                    try {
                        // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
                        if (completeProductContainer is CompleteRingContainer) {
                            println("${panelInstanceName()} -> Call ICompleteProductContainer.update(newRing, newStone)")
                            completeProductContainer.update(newRing, newStone)
                        }

                    } catch (ex: Exception) {
                        println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
                        println("Illegal Cast?")
                    }
                }

                else -> {
                    // TODO
                }
            }

            // TODO Is this necessary?
            completeProductContainer.relayout()

            relayout()

        } catch (ex: Exception) {
            println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
            println("${panelInstanceName()} currentAccessory = $currentAccessory")
        }
    }

    private fun getLargeNecklace(): Necklace {
        val baseNecklaceMetadata = NecklaceStoreMetadata.getLargeNecklaceMetadata(currentBaseProduct.name!!)
        val newNecklaceName: String = baseNecklaceMetadata.first
        val newNecklaceFile: String = baseNecklaceMetadata.second
        val newNecklaceImage = mainScope.async { newNecklaceFile.let { images.load(it) }!! }
        return Necklace(newNecklaceName, newNecklaceFile, newNecklaceImage)
    }

    private fun getLargeRing(): Ring {
        val largeRingMetadata = getLargeRingMetadata(currentBaseProduct.name!!)
        val newRingName: String = largeRingMetadata.first
        val newRingFile: String = largeRingMetadata.second
        val newRingImage = mainScope.async { newRingFile.let { images.load(it) }!! }
        return Ring(newRingName, newRingFile, newRingImage)
    }
}