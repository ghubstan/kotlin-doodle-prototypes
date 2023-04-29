package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.model.PasswordConfirmation
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.cpfField
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher


class RegistrationForm(
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
    // Submit account registration details;  cadastrar.

    private var registrationProfile: RegistrationProfile? = null

    private val submit = submitButton("Cadastrar").apply {
        fired += {
            println("TODO: Register with $registrationProfile")
            // mainScope.launch { /* eventBus.produceEvent(loginEvent) */ }
        }
    }

    // TODO How do I nest this in the form below?
    private val setPasswordForm = SetPasswordForm(
        submit = submit,
        pageType = pageType,
        config = config,
        uiDispatcher = uiDispatcher,
        animator = animator,
        pathMetrics = pathMetrics,
        fonts = fonts,
        theme = theme,
        themes = themes,
        images = images,
        textMetrics = textMetrics,
        textFieldStyler = textFieldStyler,
        linkStyler = linkStyler,
        focusManager = focusManager,
        popups = popups,
        modals = modals,
        menuEventBus = menuEventBus,
        baseProductSelectEventBus = baseProductSelectEventBus,
        accessorySelectEventBus = accessorySelectEventBus
    )

    private val mainForm = Form {
        this(
            +labeled(
                name = "Nome Completo",
                help = "9+ alpha-numeric characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{9,}"),
                    config = textFieldConfig("Informar seu nome completo")
                )
            },

            +labeled(
                name = "CPF",
                help = "Informar seu CPF, no formato DDD.DDD.DDD-DD",
                showRequired = Always("*")
            ) {
                cpfField(appConfig = config)
            },


            /*
            +labeled(
                name = "Dummy CPF",
                help = "14 characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{14,}"),
                    config = textFieldConfig("Informar seu CPF, no formato DDD.DDD.DDD-DD")
                )
            },
             */

            +labeled(
                name = "Telefone Celular",
                help = "15 characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{15,}"),
                    config = textFieldConfig("Informar o número completo no formato (DD) 99999-9999")
                )
            },
            +labeled(
                name = "E-mail",
                help = "11 characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{11,}"),
                    config = textFieldConfig("Informar um E-mail válido")
                )
            },
            // Use a custom field for password/verify-password that returns a String (password).
            // It has 2 text fields that do validation to make sure they are the same.
            PasswordConfirmation("", "") to setPasswordForm.subForm,
            onInvalid = { submit.enabled = false }
        ) { (fullName, cpf, cellPhone, email, passwordConfirm) -> // destructure given list

            val passwordConfirm = passwordConfirm as PasswordConfirmation

            if (!passwordConfirm.isMatch()) {
                // TODO Set Error Msg TextField defined in View, outside of Form.
                println("Passwords do not match.")
                submit.enabled = false
            } else if (!passwordConfirm.isValid()) {
                // TODO Set Error Msg TextField defined in View, outside of Form.
                println("Password is missing required mix of characters.")
                submit.enabled = false
            } else {
                submit.enabled = true
                registrationProfile = RegistrationProfile(
                    fullName as String,
                    cpf as String,
                    cellPhone as String,
                    email as String,
                    passwordConfirm.password
                )
            }
        }
    }.apply {
        size = Size(300, 100)
        font = config.formTextFieldFont
        layout = verticalLayout(this, spacing = 12.0, itemHeight = 30.0)
    }

    init {
        size = Size(300, 300)
        children += mainForm
        children += submit
        layout = constrain(mainForm, submit) { (mainFormBounds, buttonBounds) ->
            mainFormBounds.top eq 2
            mainFormBounds.left eq parent.width * 0.10
            mainFormBounds.right eq parent.width * 0.90

            buttonBounds.top eq mainFormBounds.bottom + 10
            buttonBounds.centerX eq parent.centerX
        }
    }

    // Helper to build form with 6 fields
    operator fun <T> List<T>.component6() = this[5]

    // Helper to build form with 7 fields
    operator fun <T> List<T>.component7() = this[6]

    // Helper to build form with 8 fields
    operator fun <T> List<T>.component8() = this[7]
}

