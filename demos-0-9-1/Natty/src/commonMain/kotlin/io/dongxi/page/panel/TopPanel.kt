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

            // TODO Refactor into a one-liner (map pageType to a svg file).
            RINGS -> return LazyImage(mainScope.async { images.load("page-title-rings.svg")!! }, RECT_TITLE)
            NECKLACES -> return LazyImage(mainScope.async { images.load("page-title-necklaces.svg")!! }, RECT_TITLE)
            SCAPULARS -> return LazyImage(mainScope.async { images.load("page-title-scapulars.svg")!! }, RECT_TITLE)
            BRACELETS -> return LazyImage(mainScope.async { images.load("page-title-bracelets.svg")!! }, RECT_TITLE)
            EAR_RINGS -> return LazyImage(mainScope.async { images.load("page-title-earrings.svg")!! }, RECT_TITLE)
            ABOUT -> return LazyImage(mainScope.async { images.load("page-title-about.svg")!! }, RECT_TITLE)
            BASKET -> return LazyImage(mainScope.async { images.load("page-title-basket.svg")!! }, RECT_TITLE)
            LOGIN -> return LazyImage(mainScope.async { images.load("page-title-login.svg")!! }, RECT_TITLE)
            LOGOUT -> return LazyImage(mainScope.async { images.load("page-title-logout.svg")!! }, RECT_TITLE)
            PAYMENT -> return LazyImage(mainScope.async { images.load("page-title-payment.svg")!! }, RECT_TITLE)
            REGISTER -> return LazyImage(mainScope.async { images.load("page-title-register.svg")!! }, RECT_TITLE)
        }
    }

    private fun pageTitleFile(): String {
        return when (pageType) {
            RINGS -> "page-title-rings.svg"
            NECKLACES -> "page-title-necklaces.svg"
            SCAPULARS -> "page-title-scapulars.svg"
            BRACELETS -> "page-title-bracelets.svg"
            EAR_RINGS -> "page-title-earrings.svg"
            ABOUT -> "page-title-about.svg"
            BASKET -> "page-title-basket.svg"
            HOME -> "page-title-home.svg"
            LOGIN -> "page-title-login.svg"
            LOGOUT -> "page-title-logout.svg"
            PAYMENT -> "page-title-payment.svg"
            REGISTER -> "page-title-register.svg"
        }
    }

    private companion object {
        private val RECT_TITLE: Rectangle = Rectangle(0, 0, 200, 30)
    }
}
