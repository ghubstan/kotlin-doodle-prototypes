package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.dongxi.page.panel.form.control.FormControlFactory
import org.kodein.di.DI

class BasketForm(
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
    // 1. Confirm & adjust shopping basket contents.
    // 2. Checkout -> Go to Payments form.
}