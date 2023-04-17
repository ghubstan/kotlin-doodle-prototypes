package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.LazyImage
import io.dongxi.page.Menu
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.PageType.*
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.util.ColorUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.*
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.Resizer
import io.nacular.doodle.utils.VerticalAlignment.Middle
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
    linkStyler,
    focusManager,
    popups,
    modals,
    menuEventBus,
    baseProductSelectEventBus,
    accessorySelectEventBus
) {

    private val labelPageTitle = Label(pageType.pageTitle, Middle, Center).apply {
        height = 20.0
        fitText = setOf(Width, Height)
        styledText = StyledText(text, config.titleFont, Black.paint)
    }

    private val logo = LazyImage(mainScope.async { images.load("natty-logo.svg")!! }, RECT_SHORT_TITLE)

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
        linkStyler,
        focusManager,
        popups,
        modals,
        menuEventBus,
    ).apply {
    }


    init {
        size = Size(300, 100)

        children += logo
        children += pageTitleImage
        children += menu

        layout = constrain(logo, pageTitleImage, menu) { logoBounds,
                                                         pageTitleImageBounds,
                                                         menuBounds ->

            logoBounds.top eq 5
            logoBounds.left eq 5
            logoBounds.width.preserve
            logoBounds.height.preserve

            pageTitleImageBounds.top eq 5
            pageTitleImageBounds.centerX eq parent.centerX
            pageTitleImageBounds.centerY eq parent.centerY
            pageTitleImageBounds.width.preserve
            pageTitleImageBounds.height.preserve

            menuBounds.top eq 2
            menuBounds.centerY eq parent.centerY
            menuBounds.left eq parent.width * 0.80
            menuBounds.right eq parent.right - 5
            menuBounds.height eq parent.height - 5
        }
        Resizer(this).apply { movable = false }
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, ColorUtils.antiFlashWhite())
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
        when (pageType) {
            HOME -> {
                /*
                // TODO This does work because it attempted GET from server, i.e., URL = base64-string
                val svg = SVGUtils.genPageNameSVG(pageType.pageTitle)
                return LazyPhotoView(
                    mainScope.async { images.load(svg)!! }, Rectangle(0, 0, 85, 30)
                )
                */
                return LazyImage(mainScope.async { images.load("page-title-home.svg")!! }, RECT_SHORT_TITLE)
            }

            RINGS -> {
                return LazyImage(mainScope.async { images.load("page-title-rings.svg")!! }, RECT_SHORT_TITLE)
            }

            NECKLACES -> {
                return LazyImage(mainScope.async { images.load("page-title-necklaces.svg")!! }, RECT_SHORT_TITLE)
            }

            SCAPULARS -> {
                return LazyImage(mainScope.async { images.load("page-title-scapulars.svg")!! }, RECT_LONG_TITLE)
            }

            BRACELETS -> {
                return LazyImage(mainScope.async { images.load("page-title-bracelets.svg")!! }, RECT_SHORT_TITLE)
            }

            EAR_RINGS -> {
                return LazyImage(mainScope.async { images.load("page-title-earrings.svg")!! }, RECT_SHORT_TITLE)
            }

            ABOUT -> {
                return LazyImage(mainScope.async { images.load("page-title-about.svg")!! }, RECT_SHORT_TITLE)
            }
        }
    }

    private companion object {
        private val RECT_LONG_TITLE: Rectangle = Rectangle(0, 0, 120, 30)
        private val RECT_SHORT_TITLE: Rectangle = Rectangle(0, 0, 85, 30)
    }

}
