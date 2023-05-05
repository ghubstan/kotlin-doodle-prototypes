package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.*
import io.dongxi.model.ProductCategory.*
import io.dongxi.page.PageType
import io.dongxi.page.PageType.REGISTER
import io.dongxi.page.panel.form.RegistrationForm
import io.dongxi.page.panel.form.control.FormControlFactory
import io.dongxi.storage.NecklaceStoreMetadata
import io.dongxi.storage.RingStoreMetadata.getLargeRingMetadata
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import kotlinx.coroutines.async
import org.kodein.di.DI

class CenterPanel(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI,
    formControlFactory: FormControlFactory
) : AbstractPanel(
    pageType,
    config,
    commonDI,
    formControlFactory
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

        if (pageType.productCategory == NONE) {

            if (pageType == REGISTER) {
                children += RegistrationForm(pageType, config, commonDI, formControlFactory)
                layout = constrain(children[0], fill)
            } else {
                children += listOf(completeProductContainer)
                layout = constrain(completeProductContainer, fill)
            }

        } else {
            children += listOf(completeProductContainer)
            layout = constrain(completeProductContainer, fill)
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, config.pageBackgroundColor)
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