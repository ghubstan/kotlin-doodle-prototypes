package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.system.Cursor.Companion.Pointer
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import io.nacular.measured.units.Angle
import io.nacular.measured.units.times
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

object MenuButtonFactory {

    private val mainScope = MainScope()

    private val colorPaleSilver: Color = Color(0xc9c0bbu)
    private val colorSilver: Color = Color(0xc0c0c0u)
    private val palePlum: Color = Color(0xdda0ddu)

    // See https://en.wikipedia.org/wiki/HSL_and_HSV
    private val hsvColor1: HsvColor = HsvColor(0 * Angle.degrees, 0f, 0.8f)
    private val hsvColor1ToColor = hsvColor1.toRgb()

    private val hslColor: HslColor = HslColor(0 * Angle.degrees, 0f, 0.8f)
    private val hslColor1ToColor = hslColor.toRgb()

    fun createMenuButton(
        config: DongxiConfig,
        buttonText: String,
        tooltipText: String?,
        textMetrics: TextMetrics,
        focusManager: FocusManager,
        menuEvent: MenuEvent,
        menuEventBus: MenuEventBus
    ): PushButton {
        return PushButton(buttonText).apply {
            acceptsThemes = false
            size = Size(113, 40)
            cursor = Pointer
            toolTipText = tooltipText ?: ""
            horizontalAlignment = Center
            verticalAlignment = Middle
            behavior = simpleTextButtonRenderer(textMetrics) { button, canvas ->

                when {
                    button.model.pointerOver -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Black, thickness = 6.0),
                        // color = colorPaleSilver,
                        color = hsvColor1ToColor,
                        radius = 10.0
                    )

                    else -> canvas.rect(
                        bounds.atOrigin,
                        stroke = Stroke(color = Darkgray, thickness = 3.0),
                        color = colorSilver,
                        radius = 10.0
                    )
                }

                canvas.text(
                    text = button.text,
                    at = textPosition(button, button.text),
                    fill = Black.paint,
                    font = font
                )
            }
            fired += {
                // TODO Emit menu event.
                println("FIRE ${menuEvent.name} EVENT")
                mainScope.launch {
                    menuEventBus.produceEvent(menuEvent)
                }
            }
        }
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}
