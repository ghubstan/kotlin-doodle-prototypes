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
import io.nacular.doodle.theme.native.NativeTextFieldStyler
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
    textFieldStyler: NativeTextFieldStyler,
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
    textFieldStyler,
    linkStyler,
    focusManager,
    popups,
    modals,
    menuEventBus,
    baseProductSelectEventBus,
    accessorySelectEventBus
) {

    private val completeProductContainer = when (pageType.productCategory) {
        // TODO Find out why I cannot reduce the # of ICompleteProductContainer
        //  impls to 1  because of failed center panel image updates.  But it
        //  it probably OK to have separate AbstractCompleteProductContainer
        //  subclasses for each product category, so leave it as it is for now.
        NECKLACE -> getCompleteNecklaceContainer()
        RING -> getCompleteRingContainer()
        else -> getDummyBaseProductsContainer()
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
        // println("${panelInstanceName()} layoutForCurrentProductCategory -> currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        // println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")
        relayout()
    }

    override fun layoutForCurrentAccessorySelection() {
        // println("${panelInstanceName()} layoutForCurrentBaseProductSelection -> currentAccessory: $currentAccessory")
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
                        // TODO Find out why I cannot reduce the # of ICompleteProductContainer impls to 1
                        //  because of failed center panel imag updates.
                        if (completeProductContainer is CompleteNecklaceContainer) {
                            // println("${panelInstanceName()} -> Call ICompleteProductContainer.update(newNecklace, newPendant)")
                            completeProductContainer.update(newNecklace, newPendant)
                        }

                    } catch (ex: Exception) {
                        println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
                    }
                }

                RING -> {
                    val newRing = getLargeRing()
                    val newStone = RingStone(currentAccessory.name!!, currentAccessory.file!!, currentAccessory.image!!)

                    try {
                        // TODO Find out why I cannot reduce the # of ICompleteProductContainer impls to 1
                        //  because of failed center panel imag updates.
                        if (completeProductContainer is CompleteRingContainer) {
                            // println("${panelInstanceName()} -> Call ICompleteProductContainer.update(newRing, newStone)")
                            completeProductContainer.update(newRing, newStone)
                        }

                    } catch (ex: Exception) {
                        println("EXCEPTION ${panelInstanceName()} -> layoutForCompletedJewel():  $ex")
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
        val nameFileTuple = NecklaceStoreMetadata.getLargeNecklaceMetadata(currentBaseProduct.name!!)
        val name: String = nameFileTuple.first
        val file: String = nameFileTuple.second
        val image = mainScope.async { file.let { images.load(it) }!! }
        return Necklace(name, file, image)
    }

    private fun getLargeRing(): Ring {
        val nameFileTuple = getLargeRingMetadata(currentBaseProduct.name!!)
        val name: String = nameFileTuple.first
        val file: String = nameFileTuple.second
        val image = mainScope.async { file.let { images.load(it) }!! }
        return Ring(name, file, image)
    }
}