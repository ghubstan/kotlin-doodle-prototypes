package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.FormControlFactory
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.form.LabeledConfig
import io.nacular.doodle.controls.form.TextFieldConfig
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.text.invoke
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher
import org.kodein.di.DI
import org.kodein.di.instance

abstract class AbstractForm(
    val pageType: PageType,
    val config: DongxiConfig,
    val commonDI: DI,
    val formControlFactory: FormControlFactory
) : Container() {

    val animator: Animator by commonDI.instance<Animator>()
    val focusManager: FocusManager by commonDI.instance<FocusManager>()
    val fonts: FontLoader by commonDI.instance<FontLoader>()
    val images: ImageLoader by commonDI.instance<ImageLoader>()
    val linkStyler: NativeHyperLinkStyler by commonDI.instance<NativeHyperLinkStyler>()
    val modals: ModalManager by commonDI.instance<ModalManager>()
    val pathMetrics: PathMetrics by commonDI.instance<PathMetrics>()
    val popups: PopupManager by commonDI.instance<PopupManager>()
    val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()
    val theme: DynamicTheme by commonDI.instance<DynamicTheme>()
    val themes: ThemeManager by commonDI.instance<ThemeManager>()
    val uiDispatcher: CoroutineDispatcher by commonDI.instance<CoroutineDispatcher>()

    val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    val baseProductSelectEventBus: BaseProductSelectEventBus by commonDI.instance<BaseProductSelectEventBus>()
    val accessorySelectEventBus: AccessorySelectEventBus by commonDI.instance<AccessorySelectEventBus>()

    /**
     * TextField Config used for `labeled` controls.
     */
    fun <T> LabeledConfig.textFieldConfig(
        placeHolder: String = "",
        errorText: StyledText? = null
    ): TextFieldConfig<T>.() -> Unit = {
        val initialHelperText = help.styledText

        help.font = config.smallFont
        textField.placeHolder = placeHolder
        onValid = { help.styledText = initialHelperText }
        onInvalid = { it ->
            if (!textField.hasFocus) {
                help.styledText = errorText ?: it.message?.let { Color.Red(it) } ?: help.styledText
            }
        }
    }

    fun submitButton(buttonText: String) = PushButton(buttonText).apply {
        size = Size(108, 40)
        cursor = Cursor.Pointer
        acceptsThemes = false
        enabled = false // Do not allow null inputs;  only enable after valid data has been entered.
        behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
            val color = Color.Lightgray.let { if (enabled) it.darker(0.20f) else it }
            canvas.rect(bounds.atOrigin, radius = 5.0, fill = color.paint)
            canvas.text(
                button.text,
                at = textPosition(button = button, text = button.text), // TODO How do I center the text int he button?
                fill = Black.paint,
                font = config.buttonFont
            )
        }
        // The fired: ChangeObserver must be defined in the using subclass.
        // fired += { mainScope.launch {eventBus.produceEvent(event)} }
    }
}
