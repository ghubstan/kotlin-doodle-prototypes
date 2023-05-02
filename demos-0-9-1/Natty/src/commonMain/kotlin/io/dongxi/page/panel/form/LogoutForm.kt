package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.dongxi.page.panel.form.control.FormControlFactory
import org.kodein.di.DI

class LogoutForm(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI,
    formControlFactory: FormControlFactory
) : AbstractForm(
    pageType,
    config,
    commonDI,
    formControlFactory
) {
    // Confirm Logout.  Could be "in" a Modal?
}