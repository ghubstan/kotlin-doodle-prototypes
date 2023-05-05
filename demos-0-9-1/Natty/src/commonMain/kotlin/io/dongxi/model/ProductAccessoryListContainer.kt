package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import org.kodein.di.DI

class ProductAccessoryListContainer(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI
) : IProductAccessoryListContainer, AbstractProductAccessoryListContainer(
    pageType,
    config,
    commonDI
) {

    private val defaultBaseProductName = "A"

    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        loadModel(defaultBaseProductName)
        children += debugLabel
        children += scrollableList
        layout = constrain(debugLabel, scrollableList) { debugLabelBounds, listBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            listBounds.top eq debugLabelBounds.bottom + 5
            listBounds.left eq 5
            listBounds.width eq parent.width
            listBounds.bottom eq parent.bottom - 5
        }
    }
}
