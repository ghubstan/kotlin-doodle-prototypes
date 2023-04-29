package io.dongxi.page.panel.form.field


import io.nacular.doodle.controls.form.FieldVisualizer
import io.nacular.doodle.controls.form.Form.Field
import io.nacular.doodle.controls.form.Form.Valid
import io.nacular.doodle.controls.form.field
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.utils.Encoder
import io.nacular.doodle.utils.PassThroughEncoder

// region String

/**
 * Configuration for [textField] controls.
 *
 * @property textField used by the control.
 */
public class TextFieldConfig<T> internal constructor(public val textField: TextField) {
    /**
     * Called whenever the text field's input is invalid
     */
    public var onInvalid: (Throwable) -> Unit = {}

    /**
     * Called whenever the text field's input is valid
     */
    public var onValid: (T) -> Unit = {}
}

/**
 * Creates a [TextField] control that is bounded to a [Field].
 * The associated field will only be valid if the text field's input matches
 * [pattern] and [encoder.decode][Encoder.decode] produces a valid [T] from it.
 *
 * @param T is the type of the bounded field
 * @param pattern used to validate input to the field
 * @param encoder used to map [String] -> [T]
 * @param validator used to validate value from [encoder]
 * @param config used to control the resulting component
 */
// TODO This is the example Nick pointed me to, for my own custom form fields.
// TODO This is the example Nick pointed me to, for my own custom form fields.
// TODO This is the example Nick pointed me to, for my own custom form fields.
// TODO This is the example Nick pointed me to, for my own custom form fields.
// TODO This is the example Nick pointed me to, for my own custom form fields.
public fun <T> textField(
    pattern: Regex = Regex(".*"),
    encoder: Encoder<T, String>,
    validator: (T) -> Boolean = { true },
    config: TextFieldConfig<T>.() -> Unit = {}
): FieldVisualizer<T> = field {
    lateinit var configObject: TextFieldConfig<T>

    fun invalidateField(field: Field<T>, error: Throwable, notify: Boolean = true) {
        // Cannot assign to 'state': the setter is internal in 'Field'
        // field.state = Invalid()

        if (notify) configObject.onInvalid(error)
    }

    fun validate(field: Field<T>, value: String, notify: Boolean = true) {
        when {
            pattern.matches(value) -> {
                encoder.decode(value).onSuccess { decoded ->
                    when {
                        validator(decoded) -> {

                            // Cannot assign to 'state': the setter is internal in 'Field'
                            // field.state = Valid(decoded)

                            if (notify) configObject.onValid(decoded)
                        }

                        else -> invalidateField(field, IllegalArgumentException("Invalid"), notify)
                    }
                }.onFailure {
                    invalidateField(field, it, notify)
                }
            }

            else -> {
                invalidateField(field, IllegalArgumentException("Must match $pattern"), notify)
            }
        }
    }

    TextField().apply {
        textChanged += { _, _, new -> validate(field, new) }
        focusChanged += { _, _, hasFocus ->
            if (!hasFocus) {
                validate(field, text)
            }
        }

        configObject = TextFieldConfig(this@apply)
        config(configObject)

        when {
            initial is Valid && validator((initial as Valid<T>).value) -> encoder.encode((initial as Valid<T>).value)
                .getOrNull()?.let { text = it }

            else -> validate(field, text, notify = false)
        }
    }
}

/**
 * Creates a [TextField] control that is bounded to a [Field] (of type [String]).
 * The associated field will only be valid if the text field's input matches
 * [pattern].
 *
 * @param pattern used to validate input to the field
 * @param validator used to validate value after [pattern]
 * @param config used to control the resulting component
 */
public fun textField(
    pattern: Regex = Regex(".*"),
    validator: (String) -> Boolean = { true },
    config: TextFieldConfig<String>.() -> Unit = {}
): FieldVisualizer<String> = textField(pattern, PassThroughEncoder(), validator, config)

// endregion

private const val DEFAULT_HEIGHT = 32.0
private const val DEFAULT_SPACING = 2.0
private const val DEFAULT_FORM_SPACING = 12.0

// endregion
