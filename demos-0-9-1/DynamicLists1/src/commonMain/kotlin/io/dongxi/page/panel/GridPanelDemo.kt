package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
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
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Resizer
import kotlinx.coroutines.CoroutineDispatcher

class GridPanelDemo(
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
    modals: ModalManager
) : AbstractPanel(
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
) {
    private val leftPanel = LeftPanel(
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
    )

    private val centerPanel = CenterPanel(
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
    )

    private val rightPanel = RightPanel(
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
    )

    private val footerPanel = FooterPanel(
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
    )

    fun gridPanel(): GridPanel {
        val panel = GridPanel().apply {
            rowSpacing = { 10.0 }
            columnSpacing = { 10.0 }
            rowSizingPolicy = FitPanel // FitContent, or custom policy
            columnSizingPolicy = FitPanel // FitContent, or custom policy

            add(leftPanel, row = 0, column = 0, rowSpan = 2)
            add(centerPanel, row = 0, column = 1, rowSpan = 2)
            add(rightPanel, row = 0, column = 2, rowSpan = 2)
            add(footerPanel, row = 1, columnSpan = 3)

            size = Size(700)

            Resizer(this).apply { movable = false }
        }
        return panel
    }
}
