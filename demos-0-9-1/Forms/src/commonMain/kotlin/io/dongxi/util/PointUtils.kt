package io.dongxi.util


import io.nacular.doodle.geometry.Point
import kotlin.math.roundToInt

@Deprecated("Should not need this.")
object PointUtils {

    fun textCenterXPoint(viewWidth: Double, textWidth: Double, y: Int): Point {
        return Point(textCenterX(viewWidth, textWidth), y)
    }


    private fun textCenterX(viewWidth: Double, textWidth: Double): Double {
        return (viewWidth - textWidth.roundToInt()) / 2
    }

}