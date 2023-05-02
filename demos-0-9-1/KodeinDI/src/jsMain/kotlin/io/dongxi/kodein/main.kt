@file:Suppress("DuplicatedCode")

package io.dongxi.kodein


import io.dongxi.application.KodeinApp
import io.dongxi.di.DongxiModule
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.animation.AnimatorImpl
import io.nacular.doodle.application.Modules.Companion.FontModule
import io.nacular.doodle.application.Modules.Companion.ImageModule
import io.nacular.doodle.application.Modules.Companion.KeyboardModule
import io.nacular.doodle.application.Modules.Companion.ModalModule
import io.nacular.doodle.application.Modules.Companion.PointerModule
import io.nacular.doodle.application.Modules.Companion.PopupModule
import io.nacular.doodle.application.application
import io.nacular.doodle.coroutines.Dispatchers
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.Color.Companion.Cyan
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.impl.PathMetricsImpl
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicCheckBoxBehavior
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicLabelBehavior
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicListBehavior
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicRadioButtonBehavior
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicSliderBehavior
import io.nacular.doodle.theme.basic.BasicTheme.Companion.basicSwitchBehavior
import io.nacular.doodle.theme.native.NativeTheme.Companion.nativeHyperLinkBehavior
import io.nacular.doodle.theme.native.NativeTheme.Companion.nativeScrollPanelBehavior
import io.nacular.doodle.theme.native.NativeTheme.Companion.nativeTextFieldBehavior
import org.kodein.di.DI.Module
import org.kodein.di.bindSingleton
import org.kodein.di.instance


fun main() {
    application(modules = listOf(
        FontModule,         // A DI.Module, uses bindSingleton
        PointerModule,      // A DI.Module, uses bindSingleton
        KeyboardModule,     // A DI.Module, uses bindSingleton
        ImageModule,        // A DI.Module, uses bindSingleton
        PopupModule,        // A DI.Module, uses bindSingleton
        ModalModule,        // A DI.Module, uses bindSingleton
        basicLabelBehavior(),       // Also a DI.Module, calls bindBehavior
        nativeTextFieldBehavior(),  // Also a DI.Module, uses bindSingleton, calls bindBehavior
        basicCheckBoxBehavior(                          // Not sure where Module is declared in hierarchy.
            foregroundColor = Color.Black,
            backgroundColor = Lightgray,
            darkBackgroundColor = Color.Darkgray
        ),
        basicRadioButtonBehavior(
            foregroundColor = Color.Black,
            backgroundColor = Lightgray,
            darkBackgroundColor = Color.Darkgray
        ),
        basicSliderBehavior(
            barFill = Cyan.paint,
            knobFill = Red.paint,
            rangeFill = Lightgray.paint
            // TODO How showTicks ?
        ),
        basicSwitchBehavior(),                      // Not sure where Module is declared in hierarchy.
        nativeHyperLinkBehavior(),                  // Also a DI.Module, calls bindBehavior
        nativeScrollPanelBehavior(smoothScrolling = true),  // Also a DI.Module, uses bindSingleton, calls bindBehavior
        // Below -> basicListBehavior:  Also a DI.Module, uses bindSingleton, calls bindBehavior
        basicListBehavior(itemHeight = 60.00, evenItemColor = Color(0xe0bdbcu), oddItemColor = Color(0xe0bdbcu)),
        Module(name = "AppModule") {
            bindSingleton<Animator> { AnimatorImpl(timer = instance(), animationScheduler = instance()) }
            bindSingleton<PathMetrics> { PathMetricsImpl(svgFactory = instance()) }
        }
    )) {
        val commonDI = DongxiModule(
            animator = instance(),
            display = instance(),
            focusManager = instance(),
            uiDispatcher = Dispatchers.UI,
            fonts = instance(),
            images = instance(),
            linkStyler = instance(),
            modals = instance(),
            pathMetrics = instance(),
            popups = instance(),
            textMetrics = instance(),
            theme = instance(),
            themes = instance()
        ).commonModuleDI()

        KodeinApp(commonDI)
    }
}
