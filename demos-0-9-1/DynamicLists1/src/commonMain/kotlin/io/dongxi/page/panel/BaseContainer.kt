package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Resizer
import kotlinx.coroutines.CoroutineDispatcher

// TODO Rename BasePanel?
class BaseContainer(
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
    baseProductSelectEventBus: BaseProductSelectEventBus,
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

    private val topPanel = TopPanel(
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
    )

    private val leftPanel = LeftPanel(
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
    )

    private val centerPanel = CenterPanel(
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
    )

    private val rightPanel = RightPanel(
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
    )

    private val footerPanel = FooterPanel(
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
    )

    init {
        size = Size(100, 100)

        children += topPanel
        children += leftPanel
        children += centerPanel
        children += rightPanel
        children += footerPanel

        val inset = 5

        layout = constrain(
            topPanel,
            leftPanel,
            centerPanel,
            rightPanel,
            footerPanel
        ) { topPanelBounds,
            leftPanelBounds,
            centerPanelBounds,
            rightPanelBounds,
            footerPanelBounds ->

            topPanelBounds.top eq inset
            topPanelBounds.left eq inset
            topPanelBounds.right eq parent.right - inset
            topPanelBounds.bottom eq parent.height * 0.08

            leftPanelBounds.top eq topPanelBounds.bottom + inset
            leftPanelBounds.left eq inset
            leftPanelBounds.right eq parent.right * 0.20 - inset
            leftPanelBounds.bottom eq parent.height * 0.82

            centerPanelBounds.top eq topPanelBounds.bottom + inset
            centerPanelBounds.left eq leftPanelBounds.right + inset
            centerPanelBounds.right eq parent.right * 0.80 - inset
            centerPanelBounds.bottom eq parent.height * 0.82

            rightPanelBounds.top eq topPanelBounds.bottom + inset
            rightPanelBounds.left eq centerPanelBounds.right + inset
            rightPanelBounds.right eq parent.right - inset // The remaining horizontal space
            rightPanelBounds.bottom eq parent.height * 0.82

            footerPanelBounds.top eq leftPanelBounds.bottom + inset
            footerPanelBounds.left eq inset
            footerPanelBounds.right eq parent.right - inset // Span all 3 columns
            footerPanelBounds.bottom eq parent.bottom - inset
        }
        Resizer(this)
    }

    override fun layoutForCurrentProductCategory() {
        // println("BaseGridPanel currentProductCategory: $currentProductCategory")
    }

    override fun layoutForCurrentBaseProductSelection() {
        // println("BaseGridPanel currentBaseProduct: $currentBaseProduct")
    }

    override fun layoutForCurrentAccessorySelection() {
        // println("BaseGridPanel currentAccessory: $currentAccessory")
    }

    override fun layoutForCompletedJewel() {
        // noop
    }
}
