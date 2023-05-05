package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.NONE
import io.dongxi.page.PageType
import io.dongxi.page.panel.form.control.FormControlFactory
import io.nacular.doodle.core.View
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.utils.ObservableList
import org.kodein.di.DI

class BaseContainer(
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

    private val topPanel = TopPanel(pageType, config, commonDI, formControlFactory)
    private val leftPanel = LeftPanel(pageType, config, commonDI, formControlFactory)
    private val centerPanel = CenterPanel(pageType, config, commonDI, formControlFactory)
    private val rightPanel = RightPanel(pageType, config, commonDI, formControlFactory)
    private val footerPanel = FooterPanel(pageType, config, commonDI, formControlFactory)

    init {
        size = Size(100, 100)

        children += topPanel
        children += leftPanel
        children += centerPanel
        children += rightPanel
        children += footerPanel

        maybeSetDefaultBaseProductAndAccessory(children)

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

    private fun maybeSetDefaultBaseProductAndAccessory(children: ObservableList<View>) {
        if (currentProductCategory != NONE) {

            if (!currentBaseProduct.isSet()) {
                // Set current product in this BaseContain instance.
                setDefaultBaseProduct()
            }

            if (!currentAccessory.isSet()) {
                // Set current accessory in this BaseContain instance.
                setDefaultAccessory()
            }

            children.forEach {
                if (it is AbstractPanel) {
                    // Set current project & accessory for all child panels.
                    if (!it.currentBaseProduct.isSet()) {
                        it.currentBaseProduct = this.currentBaseProduct
                    }
                    if (!it.currentAccessory.isSet()) {
                        it.currentAccessory = this.currentAccessory
                    }
                    println("${panelInstanceName()}.${it.panelInstanceName()}.currentBaseProduct = ${it.currentBaseProduct}")
                    println("${panelInstanceName()}.${it.panelInstanceName()}.currentAccessory = ${it.currentAccessory}")
                }
            }
        }
    }
}
