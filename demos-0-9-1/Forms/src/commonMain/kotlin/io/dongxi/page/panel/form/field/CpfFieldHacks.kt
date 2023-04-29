package io.dongxi.page.panel.form.field

import io.dongxi.util.RegexUtils.isMatch
import io.dongxi.util.RegexUtils.threeDigitNumber
import io.dongxi.util.RegexUtils.twoDigitNumber
import io.nacular.doodle.controls.form.Form.Invalid
import io.nacular.doodle.controls.form.Form.Valid
import io.nacular.doodle.controls.form.field
import io.nacular.doodle.controls.form.ifValid
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.container
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.TextAlignment.Start

/*
class CpfTextFieldConfig<T> internal constructor(
    val textField: TextField,
    var placeHolder: String = "",
    var errorText: StyledText? = null,
    val config: DongxiConfig
) {
    /**
     * Called whenever the text field's input is invalid
     */
    public var onInvalid: (Throwable) -> Unit = {

    }

    /**
     * Called whenever the text field's input is valid
     */
    public var onValid: (T) -> Unit = {

    }
}
 */


/*
class CpfField(private val dongxiConfig: DongxiConfig) : Container() {

    fun cpfPart(config: TextFieldConfig<String>.() -> Unit = {}): FieldVisualizer<String> = field {
        lateinit var configObject: TextFieldConfig<String>

        val encoder: Encoder<String, String> = PassThroughEncoder()
        var validator: (String) -> Boolean = { true }
        var pattern = threeDigitNumber

        fun invalidateField(
            field: Field<String>,
            error: Throwable,
            notify: Boolean = true
        ) {
            // Cannot assign to 'state': the setter is internal in 'Field'.
            // field.state = Invalid()
            // TODO Refactor this so field.state is only set inside TextField.apply{ ... }
            if (notify) configObject.onInvalid(error)
        }

        fun validateField(field: Field<String>, value: String, notify: Boolean = true) {
            when {
                pattern.matches(value) -> {
                    encoder.decode(value).onSuccess { decoded ->
                        when {
                            validator(decoded) -> {

                                // Cannot assign to 'state': the setter is internal in 'Field'.
                                // field.state = Valid(decoded)
                                // TODO Refactor this so field.state is only set inside TextField.apply{ ... }

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
            textChanged += { _, _, new -> validateField(field, new) }
            focusChanged += { _, _, hasFocus ->
                if (!hasFocus) {
                    validateField(field, text)
                }
            }

            
            configObject = cpfFieldConfig(
                textField = this@apply,
                placeHolder = "",
                errorText = null,
                config = dongxiConfig
            )
            config(configObject)

            when {
                initial is Valid && validator((initial as Valid<String>).value) ->
                    encoder.encode((initial as Valid<String>).value).getOrNull()
                        ?.let { text = it }

                else -> validateField(field, text, notify = false)
            }
        }
    }

    private fun <String> LabeledConfig.cpfFieldConfig(
        textField: TextField,
        placeHolder: kotlin.String = "",
        errorText: StyledText? = null,
        dongxiConfig: DongxiConfig
    ): TextFieldConfig<String>.() -> Unit = {
        val initialHelperText = help.styledText

        help.font = dongxiConfig.smallFont
        textField.placeHolder = placeHolder

        /**
         * Called whenever a cpf field's input is invalid.
         */
        onValid = { help.styledText = initialHelperText }

        /**
         * Called whenever a cpf field's input is valid.
         */
        onInvalid = { it ->
            if (!textField.hasFocus) {
                help.styledText = errorText ?: it.message?.let { Color.Red(it) } ?: help.styledText
            }
        }
    }
}
 */


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

private fun validBaseDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, threeDigitNumber)
}

private fun validChecksumDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, twoDigitNumber)
}

fun cpfFieldPrototypeFunc() = field<String> {
    container {
        focusable = false // Ensure this wrapping container isn't focusable.

        // 1st Three Digits of CPF
        this += TextField().apply {
            initial.ifValid {
                text = it
            }

            textChanged += { _, _, new ->
                state = if (validBaseDigits(new)) {
                    println("Valid CPF base part 1: $new")
                    Valid(new) // update field as text changes
                } else {
                    Invalid()
                }
            }

            focusChanged += { _, _, hasFocus ->
                if (!hasFocus) {
                    state = if (validBaseDigits(text)) {
                        println("Valid CPF base part 1: $text")
                        Valid(text) // update field as text changes
                    } else {
                        Invalid()
                    }
                }
            }

            size = Size(100, 30)
        }

        layout = constrain(children[0]) { part1 ->
            part1.top eq 0
            part1.left eq 0
            part1.right eq parent.right
            part1.height eq 30
        }
    }
}

fun cpfLabelAndFieldPrototypeFunc(styledText: StyledText) = field<String> {
    container {
        focusable = false // Ensure this wrapping container isn't focusable.

        this += Label(styledText).apply {
            fitText = setOf(Width, Height)
            textAlignment = Start
        }

        this += TextField().apply {
            initial.ifValid {
                //  it // Set initial value to your text-field after validating (should I?)
                this.text = it
            }

            size = Size(100, 30)
            state = Invalid() // if what?

            state = Valid("111") // ensure field is valid at beginning
        }

        layout = constrain(children[0], children[1]) { label, text ->
            label.top eq 0
            label.left eq 0
            label.right eq 40
            label.height eq 25

            text.top eq 0
            text.left eq label.right + 5
            text.right eq parent.right
            text.height eq label.height
        }
    }
}
