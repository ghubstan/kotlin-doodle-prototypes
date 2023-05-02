package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.dongxi.view.MenuEvent.SHOW_ANEIS
import io.dongxi.view.MenuEvent.SHOW_HOME
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.buttons.HyperLink
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Lightgray
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.event.PointerListener.Companion.clicked
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class MenuPopup(private val config: DongxiConfig, commonDI: DI) : View() {

    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    private val mainScope = MainScope()

    private val menuLabel = Label("Navigate", Middle, Center).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(text, config.titleFont, Black.paint)
        foregroundColor = Color.Cyan
    }

    private val homeLink = HyperLink(
        url = "home?",
        text = "Home",
    ).apply {
        fired += {
            mainScope.launch {
                menuEventBus.produceEvent(SHOW_HOME)
            }
        }
    }

    private val ringsLink = HyperLink(
        url = "aneis?",
        text = "Aneis",
    ).apply {
        fired += {
            mainScope.launch {
                menuEventBus.produceEvent(SHOW_ANEIS)
            }
        }
    }

    init {
        size = Size(100, 200)
        children += listOf(menuLabel, homeLink, ringsLink)
        layout = constrain(menuLabel, homeLink, ringsLink) { menuLabelBounds, homeLinkBounds, ringsLinkBounds ->

            menuLabelBounds.top eq 10
            menuLabelBounds.centerX eq parent.centerX
            menuLabelBounds.height eq 24

            homeLinkBounds.top eq menuLabelBounds.bottom + 10
            homeLinkBounds.left eq 10
            homeLinkBounds.height eq 24

            ringsLinkBounds.top eq homeLinkBounds.bottom + 10
            ringsLinkBounds.left eq 10
            ringsLinkBounds.height eq 24
        }
        pointerChanged += clicked { popups.hide(this) }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Lightgray)
    }

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}