package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.util.RegexUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.form.*
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.container
import io.nacular.doodle.drawing.Color
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

            for (fieldNum in 0..2) {
                when (fieldNum) {
                    0 -> this += TextField().apply {
                        initial.ifValid {
                            text = it
                        }
                        textChanged += { _, _, new ->
                            labelConfig.help.styledText = defaultHelpMessage()

                            validatePassword(
                                subField = this@field,
                                fieldNum = fieldNum,
                                text = new,
                                labelConfig = labelConfig,
                                notify = !hasFocus
                            )
                        }
                        focusChanged += { _, _, hasFocus ->
                            if (!hasFocus) {
                                validatePassword(
                                    subField = this@field,
                                    fieldNum = fieldNum,
                                    text = text,
                                    labelConfig = labelConfig,
                                    notify = true
                                )
                            }
                        }
                        size = Size(100, 30)
                    }

                    1 -> this += fieldLabel("Confirme")

                    2 -> this += TextField().apply {
                        initial.ifValid {
                            text = it
                        }
                        textChanged += { _, _, new ->
                            labelConfig.help.styledText = defaultHelpMessage()

                            validatePassword(
                                subField = this@field,
                                fieldNum = fieldNum,
                                text = new,
                                labelConfig = labelConfig,
                                notify = !hasFocus
                            )
                        }
                        focusChanged += { _, _, hasFocus ->
                            if (!hasFocus) {
                                validatePassword(
                                    subField = this@field,
                                    fieldNum = fieldNum,
                                    text = text,
                                    labelConfig = labelConfig,
                                    notify = true
                                )
                            }
                        }
                        size = Size(100, 30)
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
                password.width eq parent.width * 0.45
                password.height eq FIELD_HEIGHT

                confirmLabel.left eq password.right + 7
                confirmLabel.bottom eq password.bottom + 12
                confirmLabel.width eq 60
                confirmLabel.height eq FIELD_HEIGHT

                confirmPassword.left eq confirmLabel.right + 2
                confirmPassword.top eq 0
                confirmPassword.width eq parent.right - 2
                confirmPassword.height eq FIELD_HEIGHT
            }

        }
    }

    private fun validatePassword(
        subField: FieldInfo<String>,
        fieldNum: Int,
        text: String,
        labelConfig: LabeledConfig,
        notify: Boolean = true
    ) {
        confirmedPassword.editPassword(fieldNum, text)

        if (isValidSubField(fieldNum, text)) {
            subField.state = if (confirmedPassword.isValid()) {
                labelConfig.help.styledText = defaultHelpMessage()
                Form.Valid(confirmedPassword.password())
            } else {
                Form.Invalid()
            }
        } else {
            if (notify) {
                labelConfig.help.styledText = subFieldErrorMessage(fieldNum)
            }
            subField.state = Form.Invalid()
        }
    }

    private fun isValidSubField(fieldNum: Int, subFieldValue: String): Boolean {
        val subFieldPattern = when (fieldNum < 3) {
            true -> RegexUtils.threeDigitNumber
            false -> RegexUtils.twoDigitNumber
        }
        return RegexUtils.isMatch(subFieldValue, subFieldPattern)
    }


    private fun subFieldErrorMessage(fieldNum: Int): StyledText {
        val errorMsg: String = when (fieldNum) {
            0 -> "Missing special chars"
            2 -> "Missing special chars"
            else -> ""
        }
        return styledErrorMessage(errorMsg)
    }


    private fun defaultHelpMessage(): StyledText {
        return StyledText(
            "Crie Sua Senha",
            config.smallFont,
            foreground = Color.Black.paint
        )
    }

    /**
     * Represents an email password in abc@def.com format.
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
        fun isValid(): Boolean {
            return true
        }

        override fun toString(): String {
            return "password = $password  confirmPassword = $confirmPassword"
        }
    }
}