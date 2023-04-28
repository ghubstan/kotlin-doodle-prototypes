package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.model.CPF
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.form.form
import io.nacular.doodle.controls.form.map
import io.nacular.doodle.controls.form.textField
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
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import io.nacular.doodle.utils.ToStringIntEncoder
import kotlinx.coroutines.CoroutineDispatcher

class CpfForm(
    submit: PushButton,
    pageType: PageType,
    config: DongxiConfig,
    uiDispatcher: CoroutineDispatcher,
    animator: Animator,
    pathMetrics: PathMetrics,
    fonts: FontLoader,
    theme: DynamicTheme,
    themes: ThemeManager,
    images: ImageLoader,
    textMetrics: TextMetrics,
    textFieldStyler: NativeTextFieldStyler,
    linkStyler: NativeHyperLinkStyler,
    focusManager: FocusManager,
    popups: PopupManager,
    modals: ModalManager,
    menuEventBus: MenuEventBus,
    baseProductSelectEventBus: BaseProductSelectEventBus,
    accessorySelectEventBus: AccessorySelectEventBus
) : AbstractForm(
    pageType,
    config,
    uiDispatcher,
    animator,
    pathMetrics,
    fonts,
    theme,
    themes,
    images,
    textMetrics,
    textFieldStyler,
    linkStyler,
    focusManager,
    popups,
    modals,
    menuEventBus,
    baseProductSelectEventBus,
    accessorySelectEventBus
) {

    var subForm = form<CPF> {
        form {
            this(
                initial.map { it.first3Digits } to textField(encoder = ToStringIntEncoder),
                initial.map { it.second3Digits } to textField(encoder = ToStringIntEncoder),
                initial.map { it.third3Digits } to textField(encoder = ToStringIntEncoder),
                initial.map { it.checkDigits } to textField(encoder = ToStringIntEncoder),
                onInvalid = {
                    submit.enabled = false
                }
            ) { a, b, c, d ->
                CPF(a, b, c, d)
            }.apply {
                size = Size(300, 100)
                focusable = false
            }
        }
    }
}
