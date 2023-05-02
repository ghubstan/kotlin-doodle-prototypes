package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.controls.text.Label.Companion
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.kodein.di.DI


class DummyBaseProductsContainer(
    private val config: DongxiConfig,
    val commonDI: DI
) : IProductListContainer, Container() {

    private val mainScope = MainScope()

    private val tempLabel = io.nacular.doodle.controls.text.Label(
        "TODO",
        Middle,
        Center
    ).apply {
        height = 24.0
        fitText = setOf(Dimension.Width)
        foregroundColor = Color.Transparent
    }


    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        children += listOf(tempLabel)
        layout = constrain(tempLabel) { tempLabelBounds ->
            tempLabelBounds.left eq 5
            tempLabelBounds.top eq 10
            tempLabelBounds.bottom eq 26
        }
    }

    override fun loadModel() {
        // noop
    }

    override fun clearModel() {
        // noop
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}
