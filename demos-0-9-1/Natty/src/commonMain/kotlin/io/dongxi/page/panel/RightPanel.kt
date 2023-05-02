package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.IAccessoryListContainer
import io.dongxi.model.IProductAccessoryListContainer
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ProductCategory.RING
import io.dongxi.page.PageType
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import org.kodein.di.DI

class RightPanel(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI
) : AbstractPanel(pageType, config, commonDI) {

    private val accessoryListContainer = when (pageType.productCategory) {
        NECKLACE -> getProductAccessoryListContainer()
        RING -> getProductAccessoryListContainer()
        else -> getDummyBaseProductsContainer()
    }

    init {
        size = Size(200, 200)
        children += listOf(accessoryListContainer)
        layout = constrain(accessoryListContainer, fill)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, config.pageBackgroundColor)
    }

    override fun layoutForCurrentProductCategory() {
        // println("${panelInstanceName()} currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")
        try {
            // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
            if (accessoryListContainer is IAccessoryListContainer) {
                accessoryListContainer.clearModel()
                accessoryListContainer.loadModel(currentBaseProduct.name ?: "A")
            }

            // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
            if (accessoryListContainer is IProductAccessoryListContainer) {
                accessoryListContainer.clearModel()
                accessoryListContainer.loadModel(currentBaseProduct.name ?: "A")
            }
        } catch (ex: Exception) {
            println("EXCEPTION ${panelInstanceName()} -> layoutForCurrentBaseProductSelection():  $ex")
        }

        accessoryListContainer.relayout()
        relayout()
    }


    override fun layoutForCurrentAccessorySelection() {
        println("${panelInstanceName()} currentAccessory: $currentAccessory")
        relayout()
    }

    override fun layoutForCompletedJewel() {
        // noop
    }
}
