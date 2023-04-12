package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.PageType.*
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Resizer
import kotlinx.coroutines.CoroutineDispatcher


interface IPageFactory {
    fun buildPage(pageType: PageType): IPage
    val pageCache: MutableMap<PageType, IPage>
}

class PageFactory(
    private val config: DongxiConfig,
    private val uiDispatcher: CoroutineDispatcher,
    private val animator: Animator,
    private val pathMetrics: PathMetrics,
    private val fonts: FontLoader,
    private val theme: DynamicTheme,
    private val themes: ThemeManager,
    private val images: ImageLoader,
    private val textMetrics: TextMetrics,
    private val linkStyler: NativeHyperLinkStyler,
    private val focusManager: FocusManager,
    private val popups: PopupManager,
    private val modals: ModalManager
) : IPageFactory {

    override val pageCache = mutableMapOf<PageType, IPage>()

    override fun buildPage(pageType: PageType): IPage {
        when (pageType) {
            HOME -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = HomePage(
                        "Casa",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }

            RINGS -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = RingsPage(
                        "Aneis",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }

            NECKLACES -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = NecklacesPage(
                        "Colares",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }

            SCAPULARS -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = ScapularsPage(
                        "Escapulários",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }


            BRACELETS -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = BraceletsPage(
                        "Pulseiras",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }


            EAR_RINGS -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = EarRingsPage(
                        "Brincos",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }


            ABOUT -> {
                if (pageCache.containsKey(pageType)) {
                    return pageCache[pageType]!!
                } else {
                    val page = AboutPage(
                        "Sobre",
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
                        modals
                    ).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    return page
                }
            }
        }
    }
}
