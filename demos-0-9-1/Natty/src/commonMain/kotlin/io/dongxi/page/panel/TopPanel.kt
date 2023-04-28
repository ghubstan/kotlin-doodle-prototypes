package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.LazyImage
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.PageType.*
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.menu.Menu
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async

class TopPanel(
    pageType: PageType,
    config: DongxiConfig,
    uiDispatcher: CoroutineDispatcher,
    animator: Animator,
    pathMetrics: PathMetrics,
    fonts: FontLoader,
    theme: DynamicTheme,
    themes: ThemeManager,
    images: ImageLoader,
    textMetrics: TextMetrics,
    textFieldStyler: NativeTextFieldStyler,
    linkStyler: NativeHyperLinkStyler,
    focusManager: FocusManager,
    popups: PopupManager,
    modals: ModalManager,
    menuEventBus: MenuEventBus,
    baseProductSelectEventBus: BaseProductSelectEventBus,  // Probably should not pass this param.
    accessorySelectEventBus: AccessorySelectEventBus
) : AbstractPanel(
    pageType,
    config,
    uiDispatcher,
    animator,
    pathMetrics,
    fonts,
    theme,
    themes,
    images,
    textMetrics,
    textFieldStyler,
    linkStyler,
    focusManager,
    popups,
    modals,
    menuEventBus,
    baseProductSelectEventBus,
    accessorySelectEventBus
) {
    private val pageTitleImage = getPageTitlePhotoView()

    private val menu = Menu(
        config,
        uiDispatcher,
        animator,
        pathMetrics,
        fonts,
        theme,
        themes,
        images,
        textMetrics,
        textFieldStyler,
        linkStyler,
        focusManager,
        popups,
        modals,
        menuEventBus,
    ).apply {
    }


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

            RINGS -> {
                return LazyImage(mainScope.async { images.load("page-title-rings.svg")!! }, RECT_TITLE)
            }

            NECKLACES -> {
                return LazyImage(mainScope.async { images.load("page-title-necklaces.svg")!! }, RECT_TITLE)
            }

            SCAPULARS -> {
                return LazyImage(mainScope.async { images.load("page-title-scapulars.svg")!! }, RECT_TITLE)
            }

            BRACELETS -> {
                return LazyImage(mainScope.async { images.load("page-title-bracelets.svg")!! }, RECT_TITLE)
            }

            EAR_RINGS -> {
                return LazyImage(mainScope.async { images.load("page-title-earrings.svg")!! }, RECT_TITLE)
            }

            ABOUT -> {
                return LazyImage(mainScope.async { images.load("page-title-about.svg")!! }, RECT_TITLE)
            }
        }
    }

    // TODO Refactor out duplicate constants.
    private companion object {
        private val RECT_TITLE: Rectangle = Rectangle(0, 0, 200, 30)
    }
}
