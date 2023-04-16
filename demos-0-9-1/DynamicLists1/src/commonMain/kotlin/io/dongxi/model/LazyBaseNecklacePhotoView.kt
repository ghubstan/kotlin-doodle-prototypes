package io.dongxi.model

import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.image.Image
import io.nacular.doodle.utils.observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job

/**
 * Release v0.9.1 LazyPhoto impl is missing var pendingImage by observable(pendingImage) ...
 *
 * TODO
 * All these LazyPhotoView implementations can be refactored to one after being sure how to use them for different
 * kinds of images.
 */
class LazyBaseNecklacePhotoView(pendingImage: Deferred<Image>, private val canvasDestination: Rectangle) : View() {

    // Do not expose the image;  it should just render it when it loads.
    // That way you can treat it like an image view.
    // private lateinit var image: Image

    private var image: Image? = null

    var pendingImage by observable(pendingImage) { _, _ ->
        image = null
        rerender()

        monitorImage()
    }

    init {
        monitorImage()
    }

    private fun monitorImage() {

        pendingImage.callOnCompleted {
            image = it

            if (size.empty) {
                size = it.size
            }

            rerender()
        }
    }

    override fun render(canvas: Canvas) {
        image?.let { canvas.image(image = it, destination = canvasDestination) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Deferred<Image>.callOnCompleted(block: (Image) -> Unit) {
        job.invokeOnCompletion {
            if (it == null) {
                block(this.getCompleted())
            }
        }
    }
}



