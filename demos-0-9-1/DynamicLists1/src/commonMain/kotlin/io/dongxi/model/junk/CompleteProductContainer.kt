package io.dongxi.model.junk

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.*
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher

@Deprecated("Unused due to inheritance problems (in javascript?)")
// TODO Do not delete until I find out why using this fails to consistently
//      update the center panel's complete product images.
class CompleteProductContainer(
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
) : ICompleteProductContainer, AbstractCompleteProductContainer(
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
    private val accessoryPhotoLeftBounds: Int = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()
        NECKLACE -> 83
        RING -> 50
        SCAPULAR -> TODO()
        NONE -> TODO()
    }

    private val accessoryPhotoCenterYBounds: Int = when (pageType.productCategory) {
        BRACELET -> TODO()
        EARRING -> TODO()
        NECKLACE -> 217
        RING -> 122
        SCAPULAR -> TODO()
        NONE -> TODO()
    }

    init {
        updateDebugLabelText(product, accessory)
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