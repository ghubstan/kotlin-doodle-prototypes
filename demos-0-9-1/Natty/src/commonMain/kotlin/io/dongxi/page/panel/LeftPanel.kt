package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ProductCategory.RING
import io.dongxi.page.PageType
import io.dongxi.page.panel.form.control.FormControlFactory
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import org.kodein.di.DI

class LeftPanel(
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

    private val baseProductListContainer = when (pageType.productCategory) {
        NECKLACE -> {
            getBaseProductListContainer()
        }

        RING -> {
            getBaseProductListContainer()
        }

        else -> {
            getDummyBaseProductsContainer()
        }
    }

    init {
        clipCanvasToBounds = false
        size = Size(200, 200)
        children += baseProductListContainer
        layout = constrain(baseProductListContainer, fill)
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
