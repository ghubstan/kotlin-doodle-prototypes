package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.dongxi.util.RegexUtils
import io.dongxi.util.RegexUtils.cpfPattern
import io.dongxi.util.RegexUtils.isMatch
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.ContainerBuilder
import io.nacular.doodle.core.container
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import org.kodein.di.DI


class CpfControl(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI
) : IControl, AbstractControl(
    pageType,
    config,
    commonDI
) {
    private val cpf = CPF()

    fun cpfField(labelConfig: LabeledConfig) = field {
        container {
            focusable = false // Ensure this wrapping container isn't focusable.

            // Generate 3 base digit fields (3 digits each), and a final checksum digits field (2 digits).
            for (subFieldIndex in 0..3) {
                // CPF format "DDD.DDD.DDD-DD" requires some inserted sub-field delimiter labels.
                maybeInsertDelimiterLabel(subFieldIndex, this)

                // Append sub-field of 3 or 2 digits.
                this += TextField().apply {
                    initial.ifValid {
                        text = it
                    }

                    textChanged += { _, _, new ->
                        labelConfig.help.styledText = defaultHelpMessage()

                        validateCpf(
                            subField = this@field,
                            subFieldIndex = subFieldIndex,
                            text = new,
                            labelConfig = labelConfig,
                            notify = !hasFocus
                        )
                    }

                    focusChanged += { _, _, hasFocus ->
                        if (!hasFocus) {
                            validateCpf(
                                subField = this@field,
                                subFieldIndex = subFieldIndex,
                                text = text,
                                labelConfig = labelConfig,
                                notify = true
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
                firstDigits.height eq DEFAULT_FIELD_HEIGHT

                firstDot.top eq 10
                firstDot.left eq firstDigits.right + 5
                firstDot.width.preserve
                firstDot.height.preserve

                secondDigits.top eq 0
                secondDigits.left eq firstDot.right + 5
                secondDigits.width eq 35
                secondDigits.height eq DEFAULT_FIELD_HEIGHT

                secondDot.top eq 10
                secondDot.left eq secondDigits.right + 5
                secondDot.width.preserve
                secondDot.height.preserve

                thirdDigits.top eq 0
                thirdDigits.left eq secondDot.right + 5
                thirdDigits.width eq 35
                thirdDigits.height eq DEFAULT_FIELD_HEIGHT

                hyphen.top eq 6
                hyphen.left eq thirdDigits.right + 5
                hyphen.width.preserve
                hyphen.height.preserve

                checksumDigits.top eq 0
                checksumDigits.left eq hyphen.right + 5
                checksumDigits.width eq 30
                checksumDigits.height eq DEFAULT_FIELD_HEIGHT
            }
        }
    }

    private fun validateCpf(
        subField: FieldInfo<String>,
        subFieldIndex: Int,
        text: String,
        labelConfig: LabeledConfig,
        notify: Boolean = true
    ) {
        cpf.edit(subFieldIndex, text)

        if (isValidSubField(subFieldIndex, text)) {
            subField.state = if (cpf.isValid()) {
                labelConfig.help.styledText = defaultHelpMessage()
                Form.Valid(cpf.value())
            } else {
                Form.Invalid()
            }
        } else {
            if (notify) {
                labelConfig.help.styledText = subFieldErrorMessage(subFieldIndex)
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

    private fun defaultHelpMessage(): StyledText {
        return StyledText(
            "Informar seu CPF, no formato DDD.DDD.DDD-DD",
            config.smallFont,
            foreground = Black.paint
        )
    }

    private fun subFieldErrorMessage(subFieldIndex: Int): StyledText {
        val errorMsg: String = when (subFieldIndex) {
            0 -> "1st field must be 3 digits"
            1 -> "2nd field must be 3 digits"
            2 -> "3rd field must be 3 digits"
            3 -> "4th field must be 2 digits"
            else -> ""
        }
        return styledErrorMessage(errorMsg)
    }

    @Suppress("unused")
    private fun invalidCpfErrorMessage(): StyledText {
        return styledErrorMessage("CPF is not valid")
    }

    private fun maybeInsertDelimiterLabel(
        subFieldIndex: Int,
        containerBuilder: ContainerBuilder
    ) {
        if (shouldInsertDotDelimiter(subFieldIndex)) {
            containerBuilder += fieldDelimiterLabel(".")
        } else if (shouldInsertHyphenDelimiter(subFieldIndex)) {
            containerBuilder += fieldDelimiterLabel("-")
        }
    }

    private fun shouldInsertDotDelimiter(subFieldIndex: Int): Boolean {
        return subFieldIndex == 1 || subFieldIndex == 2
    }

    private fun shouldInsertHyphenDelimiter(subFieldIndex: Int): Boolean {
        return subFieldIndex == 3
    }

    /**
     * Represents a CPF in format "DDD.DDD.DDD-DD".
     */
    internal class CPF {
        private var subField1: String = ""                 // 3 digits ->  Regex("\\d{3}")
        private var subField2: String = ""                 // 3 digits ->  Regex("\\d{3}")
        private var subField3: String = ""                 // 3 digits ->  Regex("\\d{3}")
        private var subFieldCheckDigits: String = ""       // 2 digits ->  Regex("\\d{2}")

        fun edit(subFieldIndex: Int, value: String) {
            when (subFieldIndex) {
                // Sub-field index is zero-based, like lists and arrays.
                0 -> this.subField1 = value
                1 -> this.subField2 = value
                2 -> this.subField3 = value
                3 -> this.subFieldCheckDigits = value
                else -> {}
            }
            println("Current full CPF ${this.value()} is valid? ${this.isValid()}")
        }

        /**
         * Returns the CPF number in format "DDD.DDD.DDD-DD".
         */
        fun value(): String {
            return toString()
        }

        /**
         * Returns true if value() returns a string in format "DDD.DDD.DDD-DD".
         */
        fun isValid(): Boolean {
            return isMatch(this.value(), cpfPattern)
        }

        override fun toString(): String {
            return "$subField1.$subField2.$subField3-$subFieldCheckDigits"
        }
    }
}