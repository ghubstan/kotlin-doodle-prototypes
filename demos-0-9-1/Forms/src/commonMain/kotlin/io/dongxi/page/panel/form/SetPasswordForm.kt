package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.model.PasswordConfirmation
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.FormControlFactory
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.modal.ModalManager
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

class SetPasswordForm(
    submit: PushButton,
    pageType: PageType,
    formControlFactory: FormControlFactory,
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
    formControlFactory,
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
    var subForm = form<PasswordConfirmation> {
        form {
            this(
                initial.map { it.password } to labeled(
                    name = "Crie Sua Senha",
                    help = "6+ alpha-numeric characters",
                    showRequired = Always("*")
                ) {
                    textField(
                        // TODO Do not try to create complex regex for "one capital letter, a digit, a ^ char".
                        //  Do the checks inside the PasswordConfirmation class.
                        pattern = Regex(pattern = ".{6,}"),
                        config = textFieldConfig("Informar uma senha (one capital letter, a digit, a ^ char")
                    )
                },
                initial.map { it.confirmPassword } to labeled(
                    name = "Confirme Sua Senha",
                    help = "6+ alpha-numeric characters",
                    showRequired = Always("*")
                ) {
                    textField(
                        // TODO Do not try to create complex regex for "one capital letter, a digit, a ^ char".
                        //  Do the checks inside the PasswordConfirmation class.
                        pattern = Regex(pattern = ".{6,}"),
                        config = textFieldConfig("Confirmar a senha")
                    )
                },
                onInvalid = {
                    submit.enabled = false
                }
            ) { password, confirmPassword ->
                PasswordConfirmation(password, confirmPassword)
            }
        }
    }
}
