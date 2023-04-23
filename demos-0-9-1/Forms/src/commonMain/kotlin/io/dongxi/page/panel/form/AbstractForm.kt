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
import io.nacular.doodle.controls.icons.PathIcon
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.TextField
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
import io.nacular.doodle.core.container
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Point
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.geometry.path
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.Insets
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.text.invoke
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldBehaviorModifier
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher

abstract class AbstractForm(
    val pageType: PageType,
    val config: DongxiConfig,
    val uiDispatcher: CoroutineDispatcher,
    val animator: Animator,
    val pathMetrics: PathMetrics,
    val fonts: FontLoader,
    val theme: DynamicTheme,
    val themes: ThemeManager,
    val images: ImageLoader,
    val textMetrics: TextMetrics,
    val textFieldStyler: NativeTextFieldStyler,
    val linkStyler: NativeHyperLinkStyler,
    val focusManager: FocusManager,
    val popups: PopupManager,
    val modals: ModalManager,
    val menuEventBus: MenuEventBus,
    val baseProductSelectEventBus: BaseProductSelectEventBus,
    val accessorySelectEventBus: AccessorySelectEventBus
) : Container() {

    val twoDigitNumber = Regex("^1[0-5]\\d|^[1-9]\\d|^[1-9]")
    val threeDigitNumber = Regex("^1[0-5]\\d|^[1-9]\\d|^[1-9]\\d|^[1-9]") // TODO Verify is correct regex.


    fun submitButton(buttonText: String) = PushButton(buttonText).apply {
        enabled = false
        size = Size(100, 32)
    }

    fun <T> LabeledConfig.formTextFieldConfig(
        placeHolder: String = "",
        errorText: StyledText? = null
    ): TextFieldConfig<T>.() -> Unit = {
        val initialHelperText = help.styledText

        help.font = config.formTextFieldFont
        textField.placeHolder = placeHolder
        onValid = { help.styledText = initialHelperText }
        onInvalid = { it ->
            if (!textField.hasFocus) {
                help.styledText = errorText ?: it.message?.let { Red(it) } ?: help.styledText
            }
        }
    }

    // TODO to DongxiConfig
    private val placeHolderColor = Color(0x9c999bu)
    private val outlineColor = Color(0xe5e7ebu)

    /**
     * Field used in Contact create/edit forms.
     */
    fun formTextField(
        dongxiConfig: DongxiConfig,   /* Define placeHolderColor, outlineColor */
        textFieldStyler: NativeTextFieldStyler,
        pathMetrics: PathMetrics,
        placeHolder: String,
        icon: String,
        regex: Regex,
        config: TextField.() -> Unit = {},
    ) = iconField(pathMetrics, icon) {

        textField(regex) {
            textField.placeHolder = placeHolder
            textField.placeHolderColor = placeHolderColor // placeHolderColor: define in DongxiConfig
            textField.acceptsThemes = false
            textField.behavior = textFieldStyler(textField, object : NativeTextFieldBehaviorModifier {
                init {
                    textField.acceptsThemes = false
                    textField.borderVisible = false
                    textField.backgroundColor = Color.Transparent
                }

                override fun renderBackground(textField: TextField, canvas: Canvas) {
                    canvas.line(
                        start = Point(0.0, textField.height - 1.0),
                        end = Point(textField.width, textField.height - 1.0),
                        Stroke(thickness = 1.0, fill = outlineColor.paint) // outlineColor: define in DongxiConfig
                    )
                }
            })

            config(textField)
        }
    }


    /**
     * A form field with an icon to its left.
     */
    fun <T> iconField(pathMetrics: PathMetrics, path: String, visualizer: () -> FieldVisualizer<T>) = field<T> {
        container {
            val icon = PathIcon<View>(path(path)!!, pathMetrics = pathMetrics)
            val iconSize = icon.size(this)
            focusable = false
            foregroundColor = Color.Black

            this += visualizer()(this@field).also {
                it.sizePreferencesChanged += { _, _, _ ->
                    relayout()
                }
            }

            render = {
                icon.render(this@container, this, at = Point(0.0, (height - iconSize.height) / 2))
            }

            layout = constrain(children[0]) {
                it.edges eq parent.edges + Insets(left = iconSize.width + 24)
                // fill(Insets(left = iconSize.width + 24))(it)
            }
        }
    }


}