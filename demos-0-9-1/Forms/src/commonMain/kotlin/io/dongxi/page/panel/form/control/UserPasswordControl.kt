package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.util.RegexUtils
import io.dongxi.util.RegexUtils.SPECIAL_CHARACTERS
import io.dongxi.util.RegexUtils.lowercaseLetter
import io.dongxi.util.RegexUtils.specialCharacter
import io.dongxi.util.RegexUtils.uppercaseLetter
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.container
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import io.nacular.doodle.utils.TextAlignment
import kotlinx.coroutines.CoroutineDispatcher


class UserPasswordControl(
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
) : IControl, AbstractControl(
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
    private val confirmedPassword = ConfirmedPassword()

    fun passwordConfirmationField(labelConfig: LabeledConfig) = field {
        container {
            focusable = false // Ensure this wrapping container isn't focusable.

            labelConfig.help.textAlignment = TextAlignment.Justify
            labelConfig.help.wrapsWords = true

            for (fieldNum in 0..2) {
                when (fieldNum) {
                    0 -> this += TextField().apply {
                        initial.ifValid {
                            text = it
                        }
                        textChanged += { _, _, new ->
                            labelConfig.help.styledText = defaultHelpMessage()

                            validatePasswordField(
                                passwordField = this@field,
                                fieldNum = fieldNum,
                                text = new,
                                labelConfig = labelConfig,
                                notify = !hasFocus
                            )
                        }
                        focusChanged += { _, _, hasFocus ->
                            if (!hasFocus) {
                                validatePasswordField(
                                    passwordField = this@field,
                                    fieldNum = fieldNum,
                                    text = text,
                                    labelConfig = labelConfig,
                                    notify = true
                                )
                            }
                        }
                        size = Size(PASSWORD_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT)
                    }

                    1 -> this += fieldLabel("Confirme", 60.0, DEFAULT_FIELD_HEIGHT.toDouble())

                    2 -> this += TextField().apply {
                        initial.ifValid {
                            text = it
                        }
                        textChanged += { _, _, new ->
                            labelConfig.help.styledText = defaultHelpMessage()

                            validatePasswordField(
                                passwordField = this@field,
                                fieldNum = fieldNum,
                                text = new,
                                labelConfig = labelConfig,
                                notify = !hasFocus
                            )
                        }
                        focusChanged += { _, _, hasFocus ->
                            if (!hasFocus) {
                                validatePasswordField(
                                    passwordField = this@field,
                                    fieldNum = fieldNum,
                                    text = text,
                                    labelConfig = labelConfig,
                                    notify = true
                                )
                            }
                        }
                        size = Size(PASSWORD_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT)
                    }
                }
            }

            layout = constrain(
                children[0],
                children[1],
                children[2]
            ) { (password, confirmLabel, confirmPassword) ->
                password.left eq 0
                password.top eq 0
                password.width.preserve
                password.height.preserve

                confirmLabel.left eq password.right + 5
                confirmPassword.top eq 0
                confirmLabel.centerY eq password.centerY
                confirmLabel.width.preserve
                confirmLabel.height.preserve

                confirmPassword.left eq confirmLabel.right + 5
                confirmPassword.top eq 0
                confirmPassword.width.preserve
                confirmPassword.height.preserve
            }
        }
    }

    private fun validatePasswordField(
        passwordField: FieldInfo<String>,
        fieldNum: Int,
        text: String,
        labelConfig: LabeledConfig,
        notify: Boolean = true
    ) {
        confirmedPassword.editPassword(fieldNum, text)

        // Only validate the "password" sub-field;  we only care that
        // the confirmation password matches the validated password.

        if (fieldNum == 2 && !confirmedPassword.passwordsMatch()) {
            // A valid password has been entered, but the confirmation password does not match.
            labelConfig.help.styledText = passwordsDoNotMatchErrorMessage()
            return;
        }

        if (isValidPassword(text)) {
            passwordField.state = if (confirmedPassword.passwordsMatch()) {
                labelConfig.help.styledText = defaultHelpMessage()
                Form.Valid(confirmedPassword.password())
            } else {
                Form.Invalid()
            }
        } else {
            if (notify) {
                labelConfig.help.styledText = passwordPatternErrorMessage()
            }
            passwordField.state = Form.Invalid()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return isValidLength(password)
                && containsLowercaseLetter(password)
                && containsUppercaseLetter(password)
                && containsSpecialCharacter(password)
    }

    private fun isValidLength(password: String): Boolean {
        return password.length in 8..32
    }

    private fun containsLowercaseLetter(password: String): Boolean {
        return RegexUtils.containsMatchIn(password, lowercaseLetter)
    }

    private fun containsUppercaseLetter(password: String): Boolean {
        return RegexUtils.containsMatchIn(password, uppercaseLetter)
    }

    private fun containsSpecialCharacter(password: String): Boolean {
        return RegexUtils.containsMatchIn(password, specialCharacter)
    }

    private fun passwordPatternErrorMessage(): StyledText {
        // In Kotlin, use parens () to avoid compiler error when breaking long string into multiple lines.
        // See https://kotlinlang.org/docs/reference/grammar.html
        // TODO How do I insert a new-line into StyledText?
        val errorMsg =
            ("Password must be between 8-32 characters, and include an uppercase letter, a lowercase letter,"
                    + " and one the following special characters: $SPECIAL_CHARACTERS")
        return styledErrorMessage(errorMsg)
    }

    private fun passwordsDoNotMatchErrorMessage(): StyledText {
        return styledErrorMessage("Passwords do not match")
    }

    private fun defaultHelpMessage(): StyledText {
        return StyledText(
            "Crie Sua Senha",
            config.smallFont,
            foreground = Black.paint
        )
    }

    /**
     * Represents an email password in abc@def.com format.
     *
     * Some valid password:
     *  aBc4!ZZZ
     *  ãBc4@ZZÊ
     *  1ZyX3?hz
     */
    internal class ConfirmedPassword {
        private var password: String = ""
        private var confirmPassword: String = ""

        fun editPassword(fieldNum: Int, text: String) {
            this.password = when (fieldNum) {
                0 -> text // changed
                else -> this.password // no change
            }
            this.confirmPassword = when (fieldNum) {
                2 -> text // changed
                else -> this.confirmPassword // no change
            }
        }

        /**
         * Returns the user password.
         */
        fun password(): String {
            return password
        }

        /**
         * Returns true if the two passwords match
         */
        fun passwordsMatch(): Boolean {
            return password == confirmPassword
        }

        override fun toString(): String {
            return "password = $password  confirmPassword = $confirmPassword"
        }
    }
}