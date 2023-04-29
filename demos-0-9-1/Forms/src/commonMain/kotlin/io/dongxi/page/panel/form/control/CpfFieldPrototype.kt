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
import io.nacular.doodle.layout.constraints.fill

/**
 * Represents a CPF in format "DDD.DDD.DDD-DD".
 */
private class CPF() {
    var first3Digits: String = ""
    var second3Digits: String = ""
    var third3Digits: String = ""
    var checkDigits: String = ""

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
                    if (isBaseDigitsSubField) {
                        state = if (validBaseDigits(new)) {
                            println("Valid CPF base digits: $new")
                            Form.Valid(new) // update field as text changes
                        } else {
                            Form.Invalid()
                        }
                    } else {
                        state = if (validChecksumDigits(new)) {
                            println("Valid CPF checksum digits: $new")
                            Form.Valid(new) // update field as text changes
                        } else {
                            Form.Invalid()
                        }
                    }

                    editCpfDigits(cpfPartIndex, new)
                }

                focusChanged += { _, _, hasFocus ->
                    if (!hasFocus) {
                        if (isBaseDigitsSubField) {
                            state = if (validBaseDigits(text)) {
                                println("Valid CPF base digits: $text")
                                Form.Valid(text) // update field as text changes
                            } else {
                                Form.Invalid()
                            }
                        } else {
                            state = if (validChecksumDigits(text)) {
                                println("Valid CPF checksum digits: $text")
                                Form.Valid(text) // update field as text changes
                            } else {
                                Form.Invalid()
                            }
                        }

                        editCpfDigits(cpfPartIndex, text)
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
        ) { (base1,
                base2,
                base3,
                checksum) ->
            base1.top eq 0
            base1.left eq 0
            base1.width eq 35
            base1.height eq 30

            base2.top eq 0
            base2.left eq base1.right + 5
            base2.width eq 35
            base2.height eq 30

            base3.top eq 0
            base3.left eq base2.right + 5
            base3.width eq 35
            base3.height eq 30

            checksum.top eq 0
            checksum.left eq base3.right + 5
            checksum.width eq 30
            checksum.height eq 30
        }
    }
}


private fun editCpfDigits(cpfPartIndex: Int, cpfPart: String) {
    when (cpfPartIndex) {

        0 -> {
            cpf.first3Digits = cpfPart
        }

        1 -> {
            cpf.second3Digits = cpfPart
        }

        2 -> {
            cpf.third3Digits = cpfPart
        }

        3 -> {
            cpf.checkDigits = cpfPart
        }

        else -> {
            // no op
        }
    }

    println("Current full CPF $cpf is valid? ${cpf.isValid()}")
}


///
///
///

fun cpfSubFieldPrototype() = field<String> {
    container {
        focusable = false // Ensure this wrapping container isn't focusable.

        this += TextField().apply {
            initial.ifValid {
                text = it
            }

            textChanged += { _, _, new ->
                state = if (validBaseDigits(new)) {
                    println("Valid CPF base digits: $new")
                    Form.Valid(new) // update field as text changes
                } else {
                    Form.Invalid()
                }
            }

            focusChanged += { _, _, hasFocus ->
                if (!hasFocus) {

                    state = if (validBaseDigits(text)) {
                        println("Valid CPF digits: $text")
                        Form.Valid(text) // update field as text changes
                    } else {
                        Form.Invalid()
                    }
                }
            }
            size = Size(35, 30)
            layout = constrain(children[0], fill)
        }
    }
}

///
///
///

private fun validBaseDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, RegexUtils.threeDigitNumber)
}

private fun validChecksumDigits(cpfPart: String): Boolean {
    return isMatch(cpfPart, RegexUtils.twoDigitNumber)
}
