package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.util.RegexUtils
import io.dongxi.util.RegexUtils.cpfPattern
import io.dongxi.util.RegexUtils.isMatch
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.ContainerBuilder
import io.nacular.doodle.core.container
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle

/**
 * Represents a CPF in format "DDD.DDD.DDD-DD".
 */
class CPF() {
    private var first3Digits: String = ""
    private var second3Digits: String = ""
    private var third3Digits: String = ""
    private var checkDigits: String = ""

    fun edit(subFieldIndex: Int, value: String) {
        // Field indexing starts at 0, like a list or array.
        when (subFieldIndex) {
            0 -> this.first3Digits = value
            1 -> this.second3Digits = value
            2 -> this.third3Digits = value
            3 -> this.checkDigits = value
            else -> {}
        }
        println("Current full CPF $this is valid? ${this.isValid()}")
    }

    fun isValid(): Boolean {
        return isMatch(this.toString(), cpfPattern)
    }

    override fun toString(): String {
        return "$first3Digits.$second3Digits.$third3Digits-$checkDigits"
    }
}

fun cpfField(
    labelConfig: LabeledConfig,
    appConfig: DongxiConfig
) = field {
    val cpf = CPF()

    container {
        focusable = false // Ensure this wrapping container isn't focusable.

        // Generate 3 base digit fields (3 digits each), and a final checksum digits field (2 digits).
        for (subFieldIndex in 0..3) {

            // CPF format "DDD.DDD.DDD-DD" requires some inserted sub-field delimiter labels.
            maybeInsertDelimiterLabel(subFieldIndex, this, appConfig)

            // Append sub-field of 3 or 2 digits.
            this += TextField().apply {
                initial.ifValid {
                    text = it
                }

                textChanged += { _, _, new ->
                    labelConfig.help.styledText = clearedErrorMessage(appConfig)

                    validateCpf(
                        cpf = cpf,
                        subField = this@field,
                        subFieldIndex = subFieldIndex,
                        text = new,
                        labelConfig = labelConfig,
                        notify = !hasFocus,
                        appConfig = appConfig
                    )
                }

                focusChanged += { _, _, hasFocus ->
                    if (!hasFocus) {
                        validateCpf(
                            cpf = cpf,
                            subField = this@field,
                            subFieldIndex = subFieldIndex,
                            text = text,
                            labelConfig = labelConfig,
                            notify = true,
                            appConfig = appConfig
                        )
                    }
                }
                size = Size(100, 30)
            }
        }

        layout = constrain(
            children[0],
            children[1],
            children[2],
            children[3],
            children[4],
            children[5],
            children[6]
        ) { (firstDigits,
                firstDot,
                secondDigits,
                secondDot,
                thirdDigits,
                hyphen,
                checksumDigits) ->
            firstDigits.top eq 0
            firstDigits.left eq 0
            firstDigits.width eq 35
            firstDigits.height eq 30

            firstDot.top eq 10
            firstDot.left eq firstDigits.right + 5
            firstDot.width.preserve
            firstDot.height.preserve

            secondDigits.top eq 0
            secondDigits.left eq firstDot.right + 5
            secondDigits.width eq 35
            secondDigits.height eq 30

            secondDot.top eq 10
            secondDot.left eq secondDigits.right + 5
            secondDot.width.preserve
            secondDot.height.preserve

            thirdDigits.top eq 0
            thirdDigits.left eq secondDot.right + 5
            thirdDigits.width eq 35
            thirdDigits.height eq 30

            hyphen.top eq 6
            hyphen.left eq thirdDigits.right + 5
            hyphen.width.preserve
            hyphen.height.preserve

            checksumDigits.top eq 0
            checksumDigits.left eq hyphen.right + 5
            checksumDigits.width eq 30
            checksumDigits.height eq 30
        }
    }
}

fun validateCpf(
    cpf: CPF,
    subField: FieldInfo<String>,
    subFieldIndex: Int,
    text: String,
    labelConfig: LabeledConfig,
    notify: Boolean = true,
    appConfig: DongxiConfig
) {
    cpf.edit(subFieldIndex, text)

    if (isValidSubField(subFieldIndex, text)) {
        subField.state = if (cpf.isValid()) {
            labelConfig.help.styledText = clearedErrorMessage(appConfig)
            Form.Valid(cpf.toString())
        } else {
            Form.Invalid()
        }
    } else {
        if (notify) {
            labelConfig.help.styledText = subFieldErrorMessage(subFieldIndex, appConfig)
        }
        subField.state = Form.Invalid()
    }
}

private fun isValidSubField(subFieldIndex: Int, subFieldValue: String): Boolean {
    val subFieldPattern = when (subFieldIndex < 3) {
        true -> RegexUtils.threeDigitNumber
        false -> RegexUtils.twoDigitNumber
    }
    return isMatch(subFieldValue, subFieldPattern)
}

private fun maybeInsertDelimiterLabel(
    subFieldIndex: Int,
    containerBuilder: ContainerBuilder,
    appConfig: DongxiConfig
) {
    if (shouldInsertDotDelimiter(subFieldIndex)) {
        containerBuilder += delimiterLabel(".", appConfig)
    } else if (shouldInsertHyphenDelimiter(subFieldIndex)) {
        containerBuilder += delimiterLabel("-", appConfig)
    }
}

private fun shouldInsertDotDelimiter(subFieldIndex: Int): Boolean {
    return subFieldIndex == 1 || subFieldIndex == 2
}

private fun shouldInsertHyphenDelimiter(subFieldIndex: Int): Boolean {
    return subFieldIndex == 3
}

private fun delimiterLabel(text: String, appConfig: DongxiConfig): Label {
    return Label(text, Middle, Center).apply {
        size = Size(5, 5)
        fitText = setOf(Width, Height)
        font = appConfig.formTextFieldDelimiterFont
    }
}

private fun subFieldErrorMessage(subFieldIndex: Int, appConfig: DongxiConfig): StyledText {
    val errorMsg: String = when (subFieldIndex) {
        0 -> "1st field must be 3 digits"
        1 -> "2nd field must be 3 digits"
        2 -> "3rd field must be 3 digits"
        3 -> "4th field must be 2 digits"
        else -> ""
    }
    return StyledText(errorMsg, appConfig.smallFont, foreground = Red.paint)
}

private fun invalidCpfErrorMessage(appConfig: DongxiConfig): StyledText {
    return StyledText("CPF is not valid", appConfig.smallFont, foreground = Red.paint)
}

private fun clearedErrorMessage(appConfig: DongxiConfig): StyledText {
    return StyledText("", appConfig.smallFont, foreground = Red.paint)
}

// Helper to build form with 6 fields
operator fun <T> List<T>.component6() = this[5]

// Helper to build form with 7 fields
operator fun <T> List<T>.component7() = this[6]