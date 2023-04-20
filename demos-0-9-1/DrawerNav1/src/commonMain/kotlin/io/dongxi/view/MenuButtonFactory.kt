package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.theme.simpleTextButtonRenderer
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Darkgray
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.Point
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.system.Cursor.Companion.Pointer
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

object MenuButtonFactory {

    private val mainScope = MainScope()

    private val colorPaleSilver: Color = Color(0xc9c0bbu)
    private val colorSilver: Color = Color(0xc0c0c0u)
    private val palePlum: Color = Color(0xdda0ddu)

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
                        color = colorPaleSilver,
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

    @Deprecated("Use simpler createMenuButton()")
    fun createMenuButtonThatDoesNotFocus(
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
            behavior = simpleTextButtonRenderer(textMetrics, focusManager) { button, canvas ->
                when {
                    button.model.pointerOver -> canvas.rect(
                        bounds.atOrigin,
                        radius = 4.0,
                        stroke = Stroke(color = palePlum, thickness = 8.0),
                        fill = palePlum.paint
                        //color = palePlum
                    )

                    else -> canvas.rect(
                        bounds.atOrigin,
                        radius = 4.0,
                        stroke = Stroke(color = colorPaleSilver, thickness = 4.0),
                        fill = Lightgray.paint
                        // color = Lightgray
                    )
                }

                val defaultTextPosition = textPosition(button, button.text)
                val adjustedTextPosition = Point(defaultTextPosition.x, defaultTextPosition.y - 3)
                val popupBackgroundColor = colorPaleSilver

                canvas.rect(
                    rectangle = bounds.atOrigin,
                    radius = 4.0,
                    stroke = Stroke(color = colorSilver, thickness = 8.0),
                    fill = popupBackgroundColor.paint
                )

                canvas.text(
                    button.text,
                    at = adjustedTextPosition,
                    fill = Black.paint,
                    font = config.menuButtonFont
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
}
