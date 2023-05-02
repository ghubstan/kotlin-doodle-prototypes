package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.panel.form.control.FormControlFactory
import io.nacular.doodle.core.View
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.kodein.di.DI

abstract class AbstractPage(
    override val pageType: PageType,
    val config: DongxiConfig,
    val commonDI: DI
) : IPage, View() {


    // Every page has it own from control factory, instead of one singleton for use in all pages.
    // TODO Use org.kodein.di.DI Provider instead of passing this down to all containers.
    val formControlFactory: FormControlFactory = FormControlFactory(pageType, config, commonDI)

    val mainScope = MainScope() // The scope of AbstractPage class (and subclasses), uses Dispatchers.Main.

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}