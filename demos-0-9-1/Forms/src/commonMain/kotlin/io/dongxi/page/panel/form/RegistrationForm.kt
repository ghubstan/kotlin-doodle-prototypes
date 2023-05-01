package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.ControlType
import io.dongxi.page.panel.form.control.ControlType.CPF
import io.dongxi.page.panel.form.control.CpfControl
import io.dongxi.page.panel.form.control.FormControlFactory
import io.dongxi.page.panel.form.control.UserPasswordControl
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
    formControlFactory: FormControlFactory,
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
    // Submit account registration details;  cadastrar.

    private var registrationProfile: RegistrationProfile? = null

    private val submit = submitButton("Cadastrar").apply {
        fired += {
            println("TODO: Register with $registrationProfile")
            // mainScope.launch { /* eventBus.produceEvent(loginEvent) */ }
        }
    }

    private val cpfControl = formControlFactory.buildControl(CPF) as CpfControl
    private val userPasswordControl = formControlFactory.buildControl(ControlType.SET_PASSWORD) as UserPasswordControl

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
                cpfControl.cpfField(
                    labelConfig = this
                )
            },
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
            +labeled(
                name = "Senha",
                help = "Crie Sua Senha",
                showRequired = Always("*")
            ) {
                userPasswordControl.passwordConfirmationField(
                    labelConfig = this
                )
            },

            onInvalid = { submit.enabled = false }
        ) { (fullName, cpf, cellPhone, email, password) -> // destructure given list
            submit.enabled = true
            registrationProfile = RegistrationProfile(
                fullName as String,
                cpf as String,
                cellPhone as String,
                email as String,
                password as String
            )
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
            mainFormBounds.left eq parent.width * 0.025
            mainFormBounds.right eq parent.width * 0.975

            buttonBounds.top eq mainFormBounds.bottom + 25
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

