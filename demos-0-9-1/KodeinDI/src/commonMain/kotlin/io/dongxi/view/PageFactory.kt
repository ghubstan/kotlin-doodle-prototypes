package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.dongxi.view.PageType.ANEIS
import io.dongxi.view.PageType.HOME
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
import org.kodein.di.DI
import org.kodein.di.instance


interface IPageFactory {
    fun buildPage(pageType: PageType): IPage
    val pageCache: MutableMap<PageType, IPage>
}

@Suppress("unused")
class PageFactory(private val config: DongxiConfig, private val commonDI: DI) : IPageFactory {

    private val animator: Animator by commonDI.instance<Animator>()
    private val focusManager: FocusManager by commonDI.instance<FocusManager>()
    private val fonts: FontLoader by commonDI.instance<FontLoader>()
    private val images: ImageLoader by commonDI.instance<ImageLoader>()
    private val linkStyler: NativeHyperLinkStyler by commonDI.instance<NativeHyperLinkStyler>()
    private val modals: ModalManager by commonDI.instance<ModalManager>()
    private val pathMetrics: PathMetrics by commonDI.instance<PathMetrics>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()
    private val theme: DynamicTheme by commonDI.instance<DynamicTheme>()
    private val themes: ThemeManager by commonDI.instance<ThemeManager>()
    private val uiDispatcher: CoroutineDispatcher by commonDI.instance<CoroutineDispatcher>()

    override val pageCache = mutableMapOf<PageType, IPage>()

    override fun buildPage(pageType: PageType): IPage {
        when (pageType) {
            HOME -> {
                return if (pageCache.containsKey(pageType)) {
                    pageCache[pageType]!!
                } else {
                    val page = HomePage(config, commonDI).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    page
                }
            }

            ANEIS -> {
                return if (pageCache.containsKey(pageType)) {
                    pageCache[pageType]!!
                } else {
                    val page = RingsPage(config, commonDI).apply {
                        Resizer(this).apply { }
                    }
                    pageCache[pageType] = page
                    page
                }
            }
        }
    }
}
