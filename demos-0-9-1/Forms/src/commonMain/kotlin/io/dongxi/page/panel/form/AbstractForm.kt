package io.dongxi.page.panel.form


import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.*
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.system.Cursor
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
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

    fun submitButton(buttonText: String) = PushButton("Submit").apply {
        size = Size(113, 40)
        cursor = Cursor.Pointer
        acceptsThemes = false
        enabled = false // Do not allow null inputs;  only enable after valid data has been entered.
        behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->
            val color = Color.Lightgray.let { if (enabled) it else it.lighter(0.33f) }
            canvas.rect(bounds.atOrigin, radius = 5.0, fill = color.paint)
            canvas.text(
                button.text,
                at = textPosition(button, button.text),
                fill = Color.Black.paint,
                font = config.buttonFont
            )
        }
        // The fired: ChangeObserver must be defined in the using subclass.
        // fired += { mainScope.launch {eventBus.produceEvent(event)} }
    }

    companion object {
        val twoDigitNumber = Regex("^1[0-5]\\d|^[1-9]\\d|^[1-9]")
        val threeDigitNumber = Regex("^1[0-5]\\d|^[1-9]\\d|^[1-9]\\d|^[1-9]") // TODO Verify is correct regex.
    }
}
