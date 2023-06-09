package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
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
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher
import org.kodein.di.DI
import org.kodein.di.instance


interface IPageFactory {
    val pageCache: MutableMap<PageType, IPage>
    fun buildPage(pageType: PageType): IPage
}

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

    private val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    private val baseProductSelectEventBus: BaseProductSelectEventBus by commonDI.instance<BaseProductSelectEventBus>()
    private val accessorySelectEventBus: AccessorySelectEventBus by commonDI.instance<AccessorySelectEventBus>()


    override val pageCache = mutableMapOf<PageType, IPage>()

    override fun buildPage(pageType: PageType): IPage {
        return if (pageCache.containsKey(pageType)) {
            pageCache[pageType]!!
        } else {
            val page = Page(pageType, config, commonDI)
            pageCache[pageType] = page
            page
        }
    }
}
