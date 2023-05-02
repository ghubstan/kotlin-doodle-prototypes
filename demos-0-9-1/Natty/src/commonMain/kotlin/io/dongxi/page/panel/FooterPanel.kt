package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.LazyImage
import io.dongxi.page.PageType
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import kotlinx.coroutines.async
import org.kodein.di.DI

class FooterPanel(pageType: PageType, config: DongxiConfig, commonDI: DI) : AbstractPanel(pageType, config, commonDI) {

    private val logo = LazyImage(mainScope.async { images.load("natty-logo-100x100.png")!! }, LOGO_RECT)

    init {
        size = Size(100, 600)
        children += logo
        layout = constrain(logo) { logoBounds ->
            logoBounds.left eq 5
            logoBounds.centerY eq parent.centerY
            logoBounds.height.preserve
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, config.pageBackgroundColor)
    }

    override fun layoutForCurrentProductCategory() {
        // noop
    }

    override fun layoutForCurrentBaseProductSelection() {
        // noop
    }

    override fun layoutForCurrentAccessorySelection() {
        // noop
    }

    override fun layoutForCompletedJewel() {
        // noop
    }

    private companion object {
        private val LOGO_RECT: Rectangle = Rectangle(0, 0, 100, 100)
    }
}
