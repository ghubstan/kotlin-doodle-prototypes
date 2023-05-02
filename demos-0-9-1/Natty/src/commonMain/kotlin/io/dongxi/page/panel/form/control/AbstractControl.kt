package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import org.kodein.di.DI

// Note Smallest iPhone display width is 715 mm
// iPhone 13 Mini:  642 mm
// See https://size-charts.com/topics/screen-size-charts/apple-iphone-size

abstract class AbstractControl(
    val pageType: PageType,
    val config: DongxiConfig,
    val commonDI: DI
) {

    fun fieldLabel(text: String, width: Double, height: Double): Label {
        return Label(text, Middle, Center).apply {
            size = Size(width, height)
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
        const val DEFAULT_FIELD_HEIGHT = 30
        val FIELD_DELIMITER_SIZE = Size(5, 5)
        const val PASSWORD_FIELD_WIDTH = 160
    }
}