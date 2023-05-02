package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.panel.BaseContainer
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import org.kodein.di.DI

class Page(override val pageType: PageType, config: DongxiConfig, commonDI: DI) : IPage,
    AbstractPage(pageType, config, commonDI) {

    private val baseContainer = BaseContainer(pageType, config, commonDI)

    init {
        children += listOf(baseContainer)
        layout = constrain(baseContainer, fill)
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.White)
    }
}