package io.dongxi.application

import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Display
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher
import org.kodein.di.DI
import org.kodein.di.bindSingleton

/**
 * Defines module(s) to be passed down into class hierarchies instead of large lists of parameters.
 */
class DongxiModule(
    animator: Animator,
    display: Display,
    focusManager: FocusManager,
    uiDispatcher: CoroutineDispatcher,
    fonts: FontLoader,
    images: ImageLoader,
    linkStyler: NativeHyperLinkStyler,
    modals: ModalManager,
    pathMetrics: PathMetrics,
    popups: PopupManager,
    textFieldStyler: NativeTextFieldStyler,
    textMetrics: TextMetrics,
    theme: DynamicTheme,
    themes: ThemeManager,
) {
    // Note:  Module's must have a name, else run-time error.
    private val commonModule = DI.Module(name = "CommonModule") {
        bindSingleton<Animator> { animator }
        bindSingleton<Display> { display }
        bindSingleton<CoroutineDispatcher> { uiDispatcher }
        bindSingleton<FocusManager> { focusManager }
        bindSingleton<FontLoader> { fonts }
        bindSingleton<ImageLoader> { images }
        bindSingleton<NativeHyperLinkStyler> { linkStyler }
        bindSingleton<ModalManager> { modals }
        bindSingleton<PathMetrics> { pathMetrics }
        bindSingleton<PopupManager> { popups }
        bindSingleton<NativeTextFieldStyler> { textFieldStyler }
        bindSingleton<TextMetrics> { textMetrics }
        bindSingleton<DynamicTheme> { theme }
        bindSingleton<ThemeManager> { themes }
        bindSingleton<MenuEventBus> { MenuEventBus() }
        bindSingleton<BaseProductSelectEventBus> { BaseProductSelectEventBus() }
        bindSingleton<AccessorySelectEventBus> { AccessorySelectEventBus() }
    }

    fun commonModuleDI(): DI {
        return DI { import(commonModule) }
    }
}
