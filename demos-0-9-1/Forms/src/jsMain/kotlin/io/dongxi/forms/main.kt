@file:Suppress("DuplicatedCode")

package io.dongxi.forms


import io.dongxi.application.FormsApp
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
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Cyan
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.Color.Companion.Transparent
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
        ModalModule,
        basicLabelBehavior(),
        nativeTextFieldBehavior(spellCheck = false),
        basicCheckBoxBehavior(
            foregroundColor = Black,
            backgroundColor = Lightgray,
            darkBackgroundColor = Darkgray
        ),
        basicRadioButtonBehavior(
            foregroundColor = Black,
            backgroundColor = Lightgray,
            darkBackgroundColor = Darkgray
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
        basicListBehavior(itemHeight = 60.00, evenItemColor = Transparent, oddItemColor = Transparent),
        Module(name = "AppModule") {
            bindSingleton<Animator> { AnimatorImpl(timer = instance(), animationScheduler = instance()) }
            bindSingleton<PathMetrics> { PathMetricsImpl(svgFactory = instance()) }
        }
    )) {
        FormsApp(
            display = instance(),
            uiDispatcher = Dispatchers.UI,
            animator = instance(),
            pathMetrics = instance(),
            fonts = instance(),
            theme = instance(),
            themes = instance(),
            images = instance(),
            textMetrics = instance(),
            textFieldStyler = instance(),
            linkStyler = instance(),
            focusManager = instance(),
            popups = instance(),
            modals = instance()
        )
    }
}
