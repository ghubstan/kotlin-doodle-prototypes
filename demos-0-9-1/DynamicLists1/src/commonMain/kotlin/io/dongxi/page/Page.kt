package io.dongxi.page

import io.dongxi.application.DongxiConfig
import io.dongxi.page.panel.BaseGridPanel
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.drawing.*
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher

class Page(
    override val pageType: PageType,
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
    baseProductSelectEventBus: BaseProductSelectEventBus
) : IPage, AbstractPage(
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
    baseProductSelectEventBus
) {

    private val labelPageTitle = Label(pageType.pageTitle, Middle, Center).apply {
        height = 26.0
        fitText = setOf(Dimension.Width)
        styledText = StyledText(text, config.titleFont, Color.Black.paint)
    }

    private val gridPanel = BaseGridPanel(
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
        baseProductSelectEventBus
    ).gridPanel()


    init {
        children += listOf(labelPageTitle, gridPanel)
        layout = constrain(labelPageTitle, gridPanel) { titleBounds, gridPanelBounds ->
            titleBounds.top eq 10
            titleBounds.centerX eq parent.centerX
            titleBounds.height eq 30

            gridPanelBounds.top eq titleBounds.bottom + 10
            gridPanelBounds.centerX eq parent.centerX
            gridPanelBounds.width eq parent.width - 10
            gridPanelBounds.bottom eq parent.bottom - 10
        }
    }

    override fun description(): String {
        return pageType.pageTitle // TODO delete?
    }

    override fun render(canvas: Canvas) {
        canvas.rect(bounds.atOrigin, Color.White)
    }
}