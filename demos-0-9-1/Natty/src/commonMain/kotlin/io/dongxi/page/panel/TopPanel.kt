package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.LazyImage
import io.dongxi.page.PageType
import io.dongxi.page.PageType.*
import io.dongxi.page.panel.form.control.FormControlFactory
import io.dongxi.page.panel.menu.Menu
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import kotlinx.coroutines.async
import org.kodein.di.DI

class TopPanel(
    pageType: PageType,
    config: DongxiConfig,
    commonDI: DI,
    formControlFactory: FormControlFactory
) : AbstractPanel(
    pageType,
    config,
    commonDI,
    formControlFactory
) {

    private val pageTitleImage = getPageTitlePhotoView()

    private val menu = Menu(config, commonDI).apply {}

    init {
        size = Size(300, 100)
        children += pageTitleImage
        children += menu

        layout = constrain(pageTitleImage, menu) { pageTitleImageBounds, menuBounds ->
            pageTitleImageBounds.top eq 0
            pageTitleImageBounds.left eq 5
            pageTitleImageBounds.width.preserve
            pageTitleImageBounds.height.preserve

            menuBounds.top eq 0
            menuBounds.left eq parent.right - 55
            menuBounds.width eq 55
            menuBounds.height eq 55
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

    private fun getPageTitlePhotoView(): LazyImage {
        // Preserve svg dimensions as are;   SVG file controls placement of left
        // justified text;  each svg file width is the same, regardless of text length.
        when (pageType) {
            HOME -> {
                /*
                // TODO This does work because it attempted GET from server, i.e., URL = base64-string
                val svg = SVGUtils.genPageNameSVG(pageType.pageTitle)
                return LazyPhotoView(
                    mainScope.async { images.load(svg)!! }, Rectangle(0, 0, 85, 30)
                )
                */
                return LazyImage(mainScope.async { images.load("page-title-home.svg")!! }, RECT_TITLE)
            }

            else -> return LazyImage(mainScope.async { images.load(pageTitleFile())!! }, RECT_TITLE)
        }
    }

    private fun pageTitleFile(): String {
        val imagesDir = "assets/images"
        return when (pageType) {
            RINGS -> "${config.imagesDir}/page-title-rings.svg"
            NECKLACES -> "${config.imagesDir}/page-title-necklaces.svg"
            SCAPULARS -> "${config.imagesDir}/page-title-scapulars.svg"
            BRACELETS -> "${config.imagesDir}/page-title-bracelets.svg"
            EAR_RINGS -> "${config.imagesDir}/page-title-earrings.svg"
            ABOUT -> "${config.imagesDir}/page-title-about.svg"
            BASKET -> "${config.imagesDir}/page-title-basket.svg"
            HOME -> "${config.imagesDir}/page-title-home.svg"
            LOGIN -> "${config.imagesDir}/page-title-login.svg"
            LOGOUT -> "${config.imagesDir}/page-title-logout.svg"
            PAYMENT -> "${config.imagesDir}/page-title-payment.svg"
            REGISTER -> "${config.imagesDir}/page-title-register.svg"
        }
    }

    private companion object {
        private val RECT_TITLE: Rectangle = Rectangle(0, 0, 200, 30)
    }
}
