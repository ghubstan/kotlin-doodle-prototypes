package io.dongxi.di

import io.dongxi.view.MenuEventBus
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
    textMetrics: TextMetrics,
    theme: DynamicTheme,
    themes: ThemeManager,
) {

    // Note:  Module's must have a name, or there will be run-time error.
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
        bindSingleton<TextMetrics> { textMetrics }
        bindSingleton<DynamicTheme> { theme }
        bindSingleton<ThemeManager> { themes }
        bindSingleton<MenuEventBus> { MenuEventBus() }
    }

    fun commonModuleDI(): DI {
        return DI { import(commonModule) }
    }
}
