package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.panels.GridPanel
import io.nacular.doodle.controls.panels.GridPanel.Companion.FitPanel
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Resizer
import kotlinx.coroutines.CoroutineDispatcher

class BaseGridPanel(
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

    fun gridPanel(): GridPanel {
        val panel = GridPanel().apply {
            // Controls how items are aligned within the grid cells. Defaults to [fill].
            cellAlignment = fill
            // Determines the space between rows. Defaults = `0.0`
            rowSpacing = { 10.0 }
            // Determines the space between columns. Defaults = `0.0`
            columnSpacing = { 10.0 }
            // Controls how rows are sized within the panel. Defaults = [FitContent]
            rowSizingPolicy = FitPanel
            // Controls how columns are sized within the panel. Defaults = [FitContent]
            columnSizingPolicy = FitPanel // FitContent, or custom policy

            // Left, Center, Right panels each occupy on column, and span 10 rows.
            add(leftPanel, row = 0, column = 0, rowSpan = 10)
            add(centerPanel, row = 0, column = 1, rowSpan = 10)
            add(rightPanel, row = 0, column = 2, rowSpan = 10)

            // Footer is one row, spanning all 3 columns.
            add(footerPanel, row = 10, columnSpan = 3, rowSpan = 1)

            // Need an initialize size, but controlled by parent's layout.
            size = Size(300, 200)

            Resizer(this).apply { movable = false }
        }
        return panel
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
