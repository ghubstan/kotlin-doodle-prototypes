package io.dongxi.model

import io.dongxi.application.DongxiConfig
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
    baseProductSelectEventBus
) {
    init {
        updateDebugLabelText()
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(debugLabel, productPhoto, accessoryPhoto)
        layout = constrain(debugLabel, productPhoto, accessoryPhoto) { debugLabelBounds,
                                                                       necklacePhotoBounds,
                                                                       pendantPhotoBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            necklacePhotoBounds.top eq debugLabelBounds.bottom + 10
            necklacePhotoBounds.left eq 10
            necklacePhotoBounds.width.preserve
            necklacePhotoBounds.height.preserve

            pendantPhotoBounds.left eq accessoryPhotoLeftBounds
            pendantPhotoBounds.centerY eq accessoryPhotoCenterYBounds
            pendantPhotoBounds.width.preserve
            pendantPhotoBounds.height.preserve
        }
    }
}