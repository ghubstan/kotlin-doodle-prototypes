package io.dongxi.page.panel.form.control

import io.dongxi.util.RegexUtils
import io.dongxi.util.RegexUtils.cpfPattern
import io.dongxi.util.RegexUtils.isMatch
import io.nacular.doodle.controls.form.Form
import io.nacular.doodle.controls.form.field
import io.nacular.doodle.controls.form.ifValid
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.container
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain

/**
 * Represents a CPF in format "DDD.DDD.DDD-DD".
 */
private class CPF() {
    var first3Digits: String = ""
    var second3Digits: String = ""
    var third3Digits: String = ""
    var checkDigits: String = ""

    fun edit(fieldIndex: Int, value: String) {
        // Field index starts at 0, like a list element.
        when (fieldIndex) {

            0 -> {
                this.first3Digits = value
            }

            1 -> {
                this.second3Digits = value
            }

            2 -> {
                this.third3Digits = value
            }

            3 -> {
                this.checkDigits = value
            }

            else -> {
                // no op
            }
        }
        println("Current full CPF $cpf is valid? ${cpf.isValid()}")
    }

    fun isValid(): Boolean {
        return isMatch(this.toString(), cpfPattern)
    }

    override fun toString(): String {
        return "$first3Digits.$second3Digits.$third3Digits-$checkDigits"
    }
}

private val cpf = CPF()

fun cpfFieldPrototype() = field<String> {
    container {

        focusable = false // Ensure this wrapping container isn't focusable.

        // Generate 3 base digit fields (3 digits each), and a final checksum digits field (2 digits).

        for (cpfPartIndex in 0..3) {
            val isBaseDigitsSubField: Boolean = cpfPartIndex < 3

            this += TextField().apply {
                initial.ifValid {
                    text = it
                }

                textChanged += { _, _, new ->
                    cpf.edit(cpfPartIndex, new)

                    if (isBaseDigitsSubField) {
                        if (validBaseDigits(new)) {
                            state = if (cpf.isValid()) {
                                Form.Valid(cpf.toString())
                            } else {
                                Form.Invalid()
                            }
                        } else {
                            state = Form.Invalid()
                        }

                    } else {
                        if (validChecksumDigits(new)) {
                            state = if (cpf.isValid()) {
                                Form.Valid(cpf.toString())
                            } else {
                                Form.Invalid()
                            }
                        } else {
                            state = Form.Invalid()
                        }
                    }
                }

                focusChanged += { _, _, hasFocus ->
                    if (!hasFocus) {
                        cpf.edit(cpfPartIndex, text)

                        if (isBaseDigitsSubField) {
                            if (validBaseDigits(text)) {
                                state = if (cpf.isValid()) {
                                    Form.Valid(cpf.toString())
                                } else {
                                    Form.Invalid()
                                }
                            } else {
                                state = Form.Invalid()
                            }

                        } else {
                            if (validChecksumDigits(text)) {
                                state = if (cpf.isValid()) {
                                    Form.Valid(cpf.toString())
                                } else {
                                    Form.Invalid()
                                }
                            } else {
                                state = Form.Invalid()
                            }
                        }
                    }
                }
                size = Size(100, 30)
            }
        }

        layout = constrain(
            children[0],
            children[1],
            children[2],
            children[3]
        ) { (part0,
                part1,
                part2,
                part3) ->
            part0.top eq 0
            part0.left eq 0
            part0.width eq 35
            part0.height eq 30

            part1.top eq 0
            part1.left eq part0.right + 5
            part1.width eq 35
            part1.height eq 30

            part2.top eq 0
            part2.left eq part1.right + 5
            part2.width eq 35
            part2.height eq 30

            part3.top eq 0
            part3.left eq part2.right + 5
            part3.width eq 30
            part3.height eq 30
        }
    }
}

private fun validBaseDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, RegexUtils.threeDigitNumber)
}

private fun validChecksumDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, RegexUtils.twoDigitNumber)
}
