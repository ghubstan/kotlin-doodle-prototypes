package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.LazyPhoto
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Camera
import io.nacular.doodle.core.View
import io.nacular.doodle.core.center
import io.nacular.doodle.core.renderProperty
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.AffineTransform.Companion.Identity
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.White
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Point
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.ChangeObservers
import io.nacular.doodle.utils.ChangeObserversImpl
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import io.nacular.measured.units.Angle.Companion.degrees
import io.nacular.measured.units.Angle.Companion.radians
import io.nacular.measured.units.times
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlin.math.PI

/**
 * My first wandering into camera and transform.  I got lost.  Leave it for now.
 */
class HomePage(
    override val config: DongxiConfig,
    override val uiDispatcher: CoroutineDispatcher,
    override val animator: Animator,
    override val pathMetrics: PathMetrics,
    override val fonts: FontLoader,
    override val theme: DynamicTheme,
    override val themes: ThemeManager,
    override val images: ImageLoader,
    override val textMetrics: TextMetrics,
    override val linkStyler: NativeHyperLinkStyler,
    override val focusManager: FocusManager,
    override val popups: PopupManager,
    override val modals: ModalManager
) : IPage, View() {

    private val mainScope = MainScope()

    private val pageTitle = Label("Casa", Middle, Center).apply {
        height = 26.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(text, config.titleFont, Black.paint)
    }

    private val side = 300.0
    private val rect = Rectangle(size = Size(side))
    private val stroke = Stroke(thickness = 2.0, fill = Color.Darkgray.paint)
    private var foldAngle by renderProperty(-90 * degrees) { _, _ -> (changed as ChangeObserversImpl).invoke() }
    private var canvasCamera by renderProperty(Camera(Point.Origin, 1000.0)) { _, _ ->
        (changed as ChangeObserversImpl).invoke()
    }
    private var canvasTransform by renderProperty(Identity.translate(z = -side / 2)) { _, _ ->
        (changed as ChangeObserversImpl).invoke()
    }
    private val changed: ChangeObservers<HomePage> = ChangeObserversImpl(this)

    private val pendingImage = mainScope.async { images.load("blue-diamond.svg")!! }
    private val diamondImage: View = LazyPhoto(pendingImage = pendingImage)

    init {
        // The camera and transform are properties you set after creating the view.
        diamondImage.camera = canvasCamera
        diamondImage.transform = canvasTransform

        children += listOf(pageTitle, diamondImage)
        layout = constrain(pageTitle, diamondImage) { titleBounds, diamondImageBounds ->
            titleBounds.top eq 10
            titleBounds.centerX eq parent.centerX
            titleBounds.height eq 30

            diamondImageBounds.centerX eq parent.centerX
            diamondImageBounds.centerY eq parent.centerY
        }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, White)
    }

    private fun clearTransforms(view: View) {
        view.transform = Identity
    }


    private fun affineTransformChaining(view: View) {
        view.transform
            .rotateY(by = 45 * degrees, around = Point(2, 4))
            .rotateX(by = PI * radians, around = Point(10, 3))
            //...
            .flipHorizontally()
    }

    private fun rotateXAroundCenter(view: View) {
        view.transform = Identity
        view.transform = Identity.rotateX(around = view.center, by = 45 * degrees)
    }

    private fun rotateYAroundCenter(view: View) {
        view.transform = Identity
        view.transform = Identity.rotateY(around = view.center, by = 45 * degrees)
    }

    private fun twoSidedView() {
        //  Example of how one might determine whether a View is facing the user.
        //  This takes the cross product of 2 vectors on the View's surface and
        //  applies its transformation and camera.
        val points = (canvasCamera.projection * canvasTransform).invoke(bounds.points.take(3))
        val faceUp = (points[1] - points[0] cross points[2] - points[1]).z > 0.0
    }

    fun renderCUBE(view: View, canvas: Canvas) {
        var transform = view.transform
        var faceLocation = Point((width - side) / 2, (height - side) / 2)
        drawFace(canvas, faceLocation, transform) // Back

        faceLocation += Point(x = side)
        transform *= Identity.rotateY(around = faceLocation, foldAngle)
        drawFace(canvas, faceLocation, transform) // Right

        faceLocation -= Point(y = side)
        drawFace(canvas, faceLocation, transform.rotateX(around = faceLocation + Point(y = side), foldAngle)) // Top

        faceLocation += Point(y = 2 * side)
        transform *= Identity.rotateX(around = faceLocation, -foldAngle)
        drawFace(canvas, faceLocation, transform) // Bottom

        faceLocation += Point(y = side)
        transform *= Identity.rotateX(around = faceLocation, -foldAngle)
        drawFace(canvas, faceLocation, transform) // Left

        faceLocation += Point(x = side)
        transform *= Identity.rotateY(around = faceLocation, foldAngle)
        drawFace(canvas, faceLocation, transform) // Front
    }

    private fun drawFace(canvas: Canvas, location: Point, transform: AffineTransform) {
        canvas.transform(transform, canvasCamera) { rect(rect.at(location), stroke) }
    }

    override fun description(): String {
        return pageTitle.text
    }

    override fun shutdown() {
        mainScope.cancel()
    }
}