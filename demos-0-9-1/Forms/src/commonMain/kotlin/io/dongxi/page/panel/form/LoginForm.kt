package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.drawing.*
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.text.invoke
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher

class LoginForm(
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
    // Submit username & password.   Could be "in" a Modal?
    private var username: String? = null
    private var password: String? = null

    private val submit = PushButton("Submit").apply {
        size = Size(113, 40)
        cursor = Cursor.Pointer
        acceptsThemes = false
        enabled = true
        behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
            val color = Color.Lightgray.let { if (enabled) it else it.grayScale() }

            canvas.rect(bounds.atOrigin, radius = 4.0, fill = color.paint)
            canvas.text(
                button.text,
                at = textPosition(button, button.text),
                fill = Color.Black.paint,
                font = config.buttonFont
            )
        }
        fired += {
            println("Submit username: $username password: $password")
        }
    }


    private val form = Form {
        this(
            +labeled("Username", help = "3+ letters") {
                textField(
                    Regex(".{3,}"),
                    config = textFieldConfig("Enter your username")
                )
            },
            +labeled("Password", help = "6+ letters") {
                textField(
                    Regex(".{6,}"),
                    config = textFieldConfig("Enter your password")
                )
            },
            onInvalid = {
                // Called when any field is updated with invalid data.
                submit.enabled = false
            }) { usernameInput: String,
                 passwordInput: String ->
            // Called each time ALL fields -- not just a single field --  are updated with valid data.
            submit.enabled = true
            println("Valid Input  usernameInput: $usernameInput  passwordInput: $passwordInput")
            username = usernameInput
            password = passwordInput
        }
    }.apply {
        // configure the Form view itself
        size = Size(300, 100)
        font = config.formTextFieldFont
        // Always use the vertical layout helper for forms.
        layout = verticalLayout(this, spacing = 32.0, itemHeight = 33.0)
    }

    private fun <T> LabeledConfig.textFieldConfig(
        placeHolder: String = "",
        errorText: StyledText? = null
    ): TextFieldConfig<T>.() -> Unit = {
        val initialHelperText = help.styledText

        help.font = config.smallFont
        textField.placeHolder = placeHolder
        onValid = { help.styledText = initialHelperText }
        onInvalid = {
            if (!textField.hasFocus) {
                help.styledText = errorText ?: it.message?.let { Color.Red(it) } ?: help.styledText
            }
        }
    }


    init {
        size = Size(300, 300)
        children += form
        children += submit
        layout = constrain(form, submit) { (formBounds, buttonBounds) ->
            formBounds.top eq 10

            buttonBounds.top eq formBounds.bottom + 32
        }
    }
}
