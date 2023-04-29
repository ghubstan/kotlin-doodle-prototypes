package io.dongxi.application

import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.Font
import io.nacular.doodle.drawing.opacity
import io.nacular.doodle.image.Image


// Note the order of parameters.  FormsApp may be messing up the font ordering.
data class DongxiConfig(
    val listFont: Font,
    val menuLinkFont: Font,
    val titleFont: Font,
    val lineColor: Color = Color(0xEDEDEDu),
    val filterFont: Font,
    val panelDebugFont: Font,
    val footerFont: Font,
    val formTextFieldFont: Font,
    val formTextFieldDelimiterFont: Font,
    val smallFont: Font,
    val buttonFont: Font,
    val headerColor: Color = Color(0xAF2F2Fu) opacity 0.15f,
    val deleteColor: Color = Color(0xCC9A9Au),
    val appBackground: Color = Color(0xF5F5F5u),
    val boldFooterFont: Font,
    val selectAllColor: Color = Color(0x737373u),
    val checkForeground: Image,
    val checkBackground: Image,
    val placeHolderFont: Font,
    val placeHolderText: String = "What needs to be done?",
    val placeHolderColor: Color = Color(0xE6E6E6u),
    val labelForeground: Color = Color(0x4D4D4Du),
    val footerForeground: Color = Color(0xBFBFBFu),
    val deleteHoverColor: Color = Color(0xAF5B5Eu),
    val taskCompletedColor: Color = Color(0xD9D9D9u),
    val clearCompletedText: String = "Clear completed",
    val textFieldBackground: Color = Color.White,
    val filterButtonForeground: Color = Color(0x777777u),

    // NEW
    val outline: Color = Color(0xe5e7ebu),
)
