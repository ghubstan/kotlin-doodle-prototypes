package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
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
import org.kodein.di.DI

class BaseProductListContainer(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI
) : IBaseProductListContainer, AbstractBaseProductListContainer(
    pageType,
    config,
    commonDI
) {
    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        loadModel()
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