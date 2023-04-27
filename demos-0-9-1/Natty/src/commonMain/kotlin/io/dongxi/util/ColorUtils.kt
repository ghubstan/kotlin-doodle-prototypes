package io.dongxi.util

import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.HslColor
import io.nacular.doodle.drawing.HsvColor
import io.nacular.doodle.drawing.toRgb
import io.nacular.measured.units.Angle
import io.nacular.measured.units.times

/**
 * Doodle Color objects based on Name/Hex mappings in  https://www.colorhexa.com/color-names
 */
object ColorUtils {

    // Natty's Color Preferences

    fun nattyAppBackgroundColor(): Color {
        return Color(0xfbfaebu)
    }

    fun nattyPageBackgroundColor(): Color {
        return Color(0xfcd5dau)
    }

    fun nattyFontColor(): Color {
        return Color(0x854a46u)
    }


    // Shades of Red

    fun palePlum(): Color {
        return Color(0xdda0ddu)
    }

    // Shades of White

    fun antiFlashWhite(): Color {
        return Color(0xf2f3f4u)
    }

    fun floralWhite(): Color {
        return Color(0xfffaf0u)
    }

    fun ghostWhite(): Color {
        return Color(0xf8f8ffu)
    }

    fun whiteSmoke(): Color {
        return Color(0xf5f5f5u)
    }

    // Shades of Silver

    fun colorPaleSilver(): Color {
        return Color(0xc9c0bbu)
    }

    fun colorSilver(): Color {
        return Color(0xc0c0c0u)
    }
}


// Note to self:  Don’t dance all over the color wheel.

// See https://en.wikipedia.org/wiki/HSL_and_HSV
// See https://www.lifewire.com/what-is-hsv-in-design-1078068
// See https://www.rapidtables.com/web/color/color-picker.html
// See https://www.rapidtables.com/web/color/color-wheel.html
// See https://www.rapidtables.com/convert/color/rgb-to-hsv.html
// See https://en.wikipedia.org/wiki/Hue
// See https://blog.datawrapper.de/beautifulcolors
// Good ones:
//          https://colorizer.org
//          https://www.w3schools.com/colors/colors_hsl.asp
//          https://www.developmenttools.com/color-picker
//          https://learn.leighcotnoir.com/artspeak/elements-color/hue-value-saturation

// Hue is the color portion of the model, expressed as a number from 0 to 360 degrees:
//      Red falls between 0 and 60 degrees.
//      Yellow falls between 61 and 120 degrees.
//      Green falls between 121 and 180 degrees.
//      Cyan falls between 181 and 240 degrees.
//      Blue falls between 241 and 300 degrees.
//      Magenta falls between 301 and 360 degrees.

// Saturation describes the amount of gray in a particular color, from 0 to 100 percent.
// Reducing this component toward zero introduces more gray and produces a faded effect. Sometimes,
// saturation appears as a range from 0 to 1, where 0 is gray, and 1 is a primary color.

// Value works in conjunction with saturation and describes the brightness or intensity of the color,
// from 0 to 100 percent, where 0 is completely black, and 100 is the brightest and reveals the most color.

// private val hsvColor1: HsvColor = HsvColor(313 * degrees, 5.0f, 83.0f) // ~ Mahogany
// private val hsvColor1: HsvColor = HsvColor(28 * degrees, 34.0f, 44.0f) // ~ Very Hot Pink (nice)
// private val hsvColor1: HsvColor = HsvColor(262 * degrees, 7.0f, 69.0f) // ~ Aqua/Green (nice)
private val hsvColor1: HsvColor = HsvColor(180 * Angle.degrees, 7.0f, 69.0f) // ~ Greyish Green (nice)

// private val hsvColor1: HsvColor = HsvColor(colorPaleSilver)
private val hsvColor1ToColor = hsvColor1.toRgb()

private val hslColor: HslColor = HslColor(0 * Angle.degrees, 0f, 0.8f)
private val hslColor1ToColor = hslColor.toRgb()

