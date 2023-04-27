package io.dongxi.application

import io.dongxi.util.ColorUtils.nattyAppBackgroundColor
import io.dongxi.util.ColorUtils.nattyPageBackgroundColor
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.Font
import io.nacular.doodle.drawing.opacity
import io.nacular.doodle.image.Image


data class DongxiConfig(
    val appBackgroundColor: Color = nattyAppBackgroundColor(),

    val boldFooterFont: Font,

    val checkForeground: Image,
    val checkBackground: Image,
    val clearCompletedText: String = "Clear completed",

    val deleteColor: Color = Color(0xCC9A9Au),
    val deleteHoverColor: Color = Color(0xAF5B5Eu),

    val filterButtonForeground: Color = Color(0x777777u),
    val filterFont: Font,
    val footerFont: Font,
    val formTextFieldFont: Font,
    val footerForeground: Color = Color(0xBFBFBFu),

    val headerColor: Color = Color(0xAF2F2Fu) opacity 0.15f,

    val labelForeground: Color = Color(0x4D4D4Du),
    val lineColor: Color = Color(0xEDEDEDu),
    val listFont: Font,

    val menuLinkFont: Font,

    val outline: Color = Color(0xe5e7ebu),

    val pageBackgroundColor: Color = nattyPageBackgroundColor(),
    val panelDebugFont: Font,
    val placeHolderFont: Font,
    val placeHolderText: String = "What needs to be done?",
    val placeHolderColor: Color = Color(0xE6E6E6u),

    val selectAllColor: Color = Color(0x737373u),

    val taskCompletedColor: Color = Color(0xD9D9D9u),
    val textFieldBackground: Color = Color.White,
    val titleFont: Font

)
