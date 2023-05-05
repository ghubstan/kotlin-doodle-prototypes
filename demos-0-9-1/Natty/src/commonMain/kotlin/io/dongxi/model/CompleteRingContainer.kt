package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import org.kodein.di.DI


class CompleteRingContainer(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI
) : ICompleteProductContainer, AbstractCompleteProductContainer(
    pageType,
    config,
    commonDI
) {

    init {
        updateDebugLabelText()
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(debugLabel, productPhoto, accessoryPhoto)
        layout = constrain(debugLabel, productPhoto, accessoryPhoto) { debugLabelBounds,
                                                                       productPhotoBounds,
                                                                       accessoryPhotoBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            productPhotoBounds.top eq debugLabelBounds.bottom + 10
            productPhotoBounds.left eq 10
            productPhotoBounds.width.preserve
            productPhotoBounds.height.preserve

            accessoryPhotoBounds.left eq accessoryPhotoLeftBounds
            accessoryPhotoBounds.centerY eq accessoryPhotoCenterYBounds
            accessoryPhotoBounds.width.preserve
            accessoryPhotoBounds.height.preserve
        }
    }
}