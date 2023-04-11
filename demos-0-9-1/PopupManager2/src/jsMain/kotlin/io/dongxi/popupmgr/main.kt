@file:Suppress("DuplicatedCode")

package io.dongxi.popupmgr


import io.dongxi.application.PopupManagerApp
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.animation.AnimatorImpl
import io.nacular.doodle.application.Modules.Companion.FontModule
import io.nacular.doodle.application.Modules.Companion.ImageModule
import io.nacular.doodle.application.Modules.Companion.KeyboardModule
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
import io.nacular.doodle.theme.basic.BasicTheme.*
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
        FontModule,
        PointerModule,
        KeyboardModule,
        ImageModule,
        PopupModule,
        basicLabelBehavior(),
        nativeTextFieldBehavior(),
        basicCheckBoxBehavior(
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
        basicSwitchBehavior(),
        nativeHyperLinkBehavior(),
        nativeScrollPanelBehavior(smoothScrolling = true),
        basicListBehavior(itemHeight = 60.00, evenItemColor = Color(0xe0bdbcu), oddItemColor = Color(0xe0bdbcu)),
        Module(name = "AppModule") {
            bindSingleton<Animator> { AnimatorImpl(timer = instance(), animationScheduler = instance()) }
            bindSingleton<PathMetrics> { PathMetricsImpl(svgFactory = instance()) }
            // bindSingleton<PopupManager> { PopupManagerImpl(instance()) }
            // bindSingleton<PopupManager> { instance() }
        }
    )) {
        // https://nacular.github.io/doodle-api/core/io.nacular.doodle.controls/-popup-manager/index.html
        PopupManagerApp(
            display = instance(),
            uiDispatcher = Dispatchers.UI,
            animator = instance(),
            pathMetrics = instance(),
            fonts = instance(),
            theme = instance(),
            themes = instance(),
            images = instance(),
            textMetrics = instance(),
            linkStyler = instance(),
            focusManager = instance(),
            popups = instance()
        )
    }
}
