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
    private val completeProductContainer = when (pageType.productCategory) {
        NECKLACE -> {
            // getCompleteNecklaceContainer() // Deprecated
            getCompleteProductContainer()
        }

        RING -> {
            // getCompleteRingContainer() // Deprecated
            getCompleteProductContainer()
        }

        else -> {
            getDummyBaseProductsContainer()
        }
    }

    init {
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(completeProductContainer)
        layout = constrain(completeProductContainer, fill)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, floralWhite())
    }

    override fun layoutForCurrentProductCategory() {
        // println("${panelInstanceName()} currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")
        relayout()
    }

    override fun layoutForCurrentAccessorySelection() {
        println("${panelInstanceName()} currentAccessory: $currentAccessory")
        relayout()
    }

    override fun layoutForCompletedJewel() {
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
                    val newNecklace = getBaseNecklace()
                    val newPendant =
                        NecklacePendant(currentAccessory.name!!, currentAccessory.file!!, currentAccessory.image!!)

                    try {
                        // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
                        if (completeProductContainer is ICompleteNecklaceContainer) {
                            completeProductContainer.update(newNecklace, newPendant)
                        }
                        // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
                        if (completeProductContainer is ICompleteProductContainer) {
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
                        if (completeProductContainer is ICompleteRingContainer) {
                            completeProductContainer.update(newRing, newStone)
                        }
                        // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
                        if (completeProductContainer is ICompleteProductContainer) {
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

            // completeProductContainer.relayout()
            relayout()

        } catch (ex: Exception) {
            println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
            println("${panelInstanceName()} currentAccessory = $currentAccessory")
        }
    }

    private fun getBaseNecklace(): Necklace {
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