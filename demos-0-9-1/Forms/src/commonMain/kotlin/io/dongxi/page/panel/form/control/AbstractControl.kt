package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher

abstract class AbstractControl(
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
) {

    fun fieldLabel(text: String): Label {
        return Label(text, Middle, Center).apply {
            size = Size(60, FIELD_HEIGHT)
            fitText = setOf(Width, Height)
            font = config.formTextFieldFont
        }
    }

    fun fieldDelimiterLabel(text: String): Label {
        return Label(text, Middle, Center).apply {
            size = FIELD_DELIMITER_SIZE
            fitText = setOf(Width, Height)
            font = config.formTextFieldDelimiterFont
        }
    }

    fun clearedErrorMessage(): StyledText {
        return StyledText("", config.smallFont, foreground = Red.paint)
    }

    fun styledErrorMessage(errorMessage: String): StyledText {
        return StyledText(errorMessage, config.smallFont, foreground = Red.paint)
    }

    // Helper to build form with 6 fields.
    operator fun <T> List<T>.component6() = this[5]

    // Helper to build form with 7 fields.
    operator fun <T> List<T>.component7() = this[6]

    // Helper to build form with 8 fields.
    operator fun <T> List<T>.component8() = this[7]

    // Helper to build form with 9 fields.
    operator fun <T> List<T>.component9() = this[8]

    // Helper to build form with 10 fields.
    operator fun <T> List<T>.component10() = this[9]

    companion object {
        const val FIELD_HEIGHT = 30
        val FIELD_DELIMITER_SIZE = Size(5, 5)
    }
}