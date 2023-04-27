package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
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


class RegistrationFormWorking(
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

    private var registrationProfile: RegistrationProfileWorking? = null

    private val submit = submitButton("Cadastrar").apply {
        fired += {
            println("TODO: Register with $registrationProfile")
            // mainScope.launch { /* eventBus.produceEvent(loginEvent) */ }
        }
    }

    private val form = Form {
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
                help = "14 characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{14,}"),
                    config = textFieldConfig("Informar seu CPF, no formato DDD.DDD.DDD-DD")
                )
            },
            +labeled(
                name = "Data de Nascimento",
                help = "10 characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{10,}"),
                    config = textFieldConfig("Informar sua data de nascimento completa, no formato DD/MM/AAAA")
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
            // You might consider creating a custom field for password/verify-password that returns a String,
            // but has 2 text fields that do validation to make sure they are the same.
            +labeled(
                name = "Crie Sua Senha",
                help = "6+ alpha-numeric characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{6,}"),
                    config = textFieldConfig("Informar uma senha (one capital letter, a digit, a ^ char")
                )
            },
            +labeled(
                name = "Confirme Sua Senha",
                help = "6+ alpha-numeric characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{6,}"),
                    config = textFieldConfig("Confirmar a senha")
                )
            },
            onInvalid = { submit.enabled = false }
        ) { (fullName, cpf, birthDate, cellPhone, email, password, passwordConfirm) -> // destructure given list
            submit.enabled = true
            registrationProfile = RegistrationProfileWorking(
                fullName as String,
                cpf as String,
                birthDate as String,
                cellPhone as String,
                email as String,
                password as String,
                passwordConfirm as String
            )

        }
    }.apply {
        size = Size(300, 100)
        font = config.formTextFieldFont
        layout = verticalLayout(this, spacing = 12.0, itemHeight = 30.0)
    }

    init {
        size = Size(300, 300)
        children += form
        children += submit
        layout = constrain(form, submit) { (formBounds, buttonBounds) ->
            formBounds.top eq 2
            formBounds.left eq parent.width * 0.10
            formBounds.right eq parent.width * 0.90

            buttonBounds.top eq formBounds.bottom + 10
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

