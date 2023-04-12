package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
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
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class RightPanel(
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
    baseProductSelectEventBus: BaseProductSelectEventBus
) : AbstractPanel(
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
    baseProductSelectEventBus
) {

    init {
        size = Size(200, 200)

        mainScope.launch {
            baseProductSelectEventBus.events.filter { event ->
                event != null // What filter predicate do I use?
            }.collectLatest {
                println("RightPanel received ${it.name} event")
                val selectedBaseProductDetail = it.baseProductDetail()
                println("RightPanel ${it.name} detail: $selectedBaseProductDetail")
            }
        }
    }
}
