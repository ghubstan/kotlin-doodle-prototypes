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
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Lightgray
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

    private var credentials: UsernamePasswordCredentials? = null

    private val submit = PushButton("Submit").apply {
        size = Size(113, 40)
        cursor = Cursor.Pointer
        acceptsThemes = false
        enabled = false // Do not allow null inputs;  only enable after valid data has been entered.
        behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
            val color = Lightgray.let { if (enabled) it else it.lighter(0.33f) }
            canvas.rect(bounds.atOrigin, radius = 5.0, fill = color.paint)
            canvas.text(
                button.text,
                at = textPosition(button, button.text),
                fill = Black.paint,
                font = config.buttonFont
            )
        }
        fired += {
            println("TODO: submit $credentials")
            // mainScope.launch { /* eventBus.produceEvent(loginEvent) */ }
        }
    }

    /**
     * Config used for `labeled` controls.
     */
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

    // Doodle forms make data collection simple, while still preserving flexibility to build just the right experience.
    // They hide a lot of the complexity associated with mapping visual components to fields, state management, and
    // validation. The result is an intuitive metaphor modeled around the idea of a constructor.
    //
    // Doodle also has a set of helpful forms controls that cover a reasonable range of data-types. These make its easy
    // to create forms without much hassle. But there are bound to be cases where more customization is needed. This is
    // why Doodle forms are also extensible, allowing you to fully customize the data they bind to and how each fields
    // is visualized.
    //
    // Forms are "very similar" to constructors in that they have typed parameter lists (fields), and can only "create"
    // instances when all their inputs are valid. Like any constructor, a Form can have optional fields, default values,
    // and arbitrary types for its fields.
    //
    // FORMS ARE NOT TYPED
    // While Forms behave "like" constructors in most ways, they do not actually create instances (only sub-forms do).
    // This means they are not typed. Instead, they take fields and output a corresponding lists of strongly-typed
    // data when all their fields are valid. This notification is intentionally general to allow forms to be used in
    // a wide range of used cases.


    /**
     * Form:  A visual component that serves as a strongly-typed constructor of some arbitrary type. Forms are very
     * similar to constructors in that they have typed parameter lists (fields), and can only "create" instances when all
     * their inputs are valid. Like any constructor, a Form can have optional fields, default values, and arbitrary
     * types for its fields.
     *
     * Forms also have a `Behavior`, `Layout` and some other properties of a Container to allow customization.
     */
    private val form = Form {
        this(
            // labeled():  Creates a component with a name [Label], the result of [visualizer] and a helper [Label] that is bound to a [Field].
            // This control simply wraps an existing one with configurable text labels.
            //      @param name used in the label
            //      @param help used as helper text
            //      @param showRequired used to indicate whether the field is required
            //      @param visualizer being decorated
            +labeled(
                name = "Username",
                help = "3+ alpha-numeric characters",
                showRequired = Always("*")
            ) {
                // textField(): FieldVisualizer<String> = textField(pattern, PassThroughEncoder(), validator, config)
                // Creates a [TextField] control that is bounded to a [Field] (of type [String]).
                // The associated field will only be valid if the text field's input matches  [pattern].
                //      @param pattern used to validate input to the field :: pattern  : Regex = Regex(".*"),
                //      @param validator used to validate value after [pattern] :: validator: (String) -> Boolean = { true }
                //      @param config used to control the resulting component :: config   : TextFieldConfig<String>.() -> Unit = {}
                textField(
                    pattern = Regex(pattern = ".{3,}"),
                    config = textFieldConfig("Enter your username")
                )
            },
            +labeled(
                name = "Password",
                help = "6+ alpha-numeric characters",
                showRequired = Always("*")
            ) {
                textField(
                    pattern = Regex(pattern = ".{6,}"),
                    config = textFieldConfig("Enter your password")
                )
            },
            onInvalid = {
                // Called when any field is updated with invalid data.
                submit.enabled = false
            }) { username: String, password: String ->
            submit.enabled = true
            // Called only after ALL fields -- not just one --  are updated with valid data.
            // Form builder DSL allows constructs as follows:
            credentials = UsernamePasswordCredentials(username, password)
        }
    }.apply {
        // configure the Form view itself
        size = Size(300, 100)
        font = config.formTextFieldFont
        // Always use the vertical layout helper for forms.
        layout = verticalLayout(this, spacing = 32.0, itemHeight = 33.0)
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

