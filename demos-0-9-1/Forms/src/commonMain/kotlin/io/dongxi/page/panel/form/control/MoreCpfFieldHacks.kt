package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.util.RegexUtils
import io.nacular.doodle.controls.form.FieldVisualizer
import io.nacular.doodle.controls.form.Form.Field
import io.nacular.doodle.controls.form.Form.Valid
import io.nacular.doodle.controls.form.LabeledConfig
import io.nacular.doodle.controls.form.TextFieldConfig
import io.nacular.doodle.controls.form.field
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.text.invoke
import io.nacular.doodle.utils.Encoder
import io.nacular.doodle.utils.PassThroughEncoder

/**
 * Configuration for [cpfSubField] controls.
 *
 * @property cpfSubField used by the control.
 */
public class CpfSubFieldConfigFailed<T>(
    private val cpfSubField: TextField,
    private val pattern: Regex = Regex(".*"),
    placeHolder: String = "",
    errorText: StyledText? = null,
    dongxiConfig: DongxiConfig
) {
    /**
     * Called whenever the CPF sub-field's input is invalid.
     */
    public var onInvalid: (Throwable) -> Unit = {

    }

    /**
     * Called whenever the CPF sub-field's input is valid.
     */
    public var onValid: (String) -> Unit = {

    }
}

/**
 * TextField Config used for `labeled` controls.
 */
fun <String> LabeledConfig.cpfFieldConfigFailed(
    placeHolder: kotlin.String = "",
    errorText: StyledText? = null,
    dongxiConfig: DongxiConfig
): TextFieldConfig<String>.() -> Unit = {
    val initialHelperText = help.styledText

    help.font = dongxiConfig.smallFont
    textField.placeHolder = placeHolder
    onValid = { help.styledText = initialHelperText }
    onInvalid = { it ->
        if (!textField.hasFocus) {
            help.styledText = errorText ?: it.message?.let { Color.Red(it) } ?: help.styledText
        }
    }
}


public fun <T> cpfFieldFailed(
    cpfSubFieldConfig: CpfSubFieldConfigFailed<String>.() -> Unit = {},
    dongxiConfig: DongxiConfig
): FieldVisualizer<String> = field {

    lateinit var configObject: CpfSubFieldConfigFailed<String>

    val encoder: Encoder<String, String> = PassThroughEncoder()
    var validator: (String) -> Boolean = { true }
    var pattern = RegexUtils.threeDigitNumber

    fun invalidateSubField(
        cpfSubField: Field<String>,
        error: Throwable,
        notify: Boolean = true
    ) {
        // Cannot assign to 'state': the setter is internal in 'Field'.
        // cpfSubField.state = Invalid()
        // TODO Refactor this so field.state is only set inside TextField.apply{ ... }
        if (notify) configObject.onInvalid(error)
    }

    fun validateSubField(cpfSubField: Field<String>, value: String, notify: Boolean = true) {
        when {
            pattern.matches(value as CharSequence) -> {
                // Cannot assign to 'state': the setter is internal in 'Field'
                // cpfSubField.state = Valid(value)
                if (notify) configObject.onValid(value)
            }

            else -> {
                invalidateSubField(field, IllegalArgumentException("Must match $pattern"), notify)
            }
        }
    }


    TextField().apply {
        textChanged += { _, _, new -> validateSubField(field, new) }
        focusChanged += { _, _, hasFocus ->
            if (!hasFocus) {
                validateSubField(field, text)
            }
        }


        configObject = CpfSubFieldConfigFailed(
            cpfSubField = this@apply,
            placeHolder = "",
            errorText = null,
            dongxiConfig = dongxiConfig
        )
        cpfSubFieldConfig(configObject)

        when {
            initial is Valid && validator((initial as Valid<String>).value) ->
                encoder.encode((initial as Valid<String>).value).getOrNull()
                    ?.let { text = it }

            else -> validateSubField(field, text, notify = false)
        }
    }
}
