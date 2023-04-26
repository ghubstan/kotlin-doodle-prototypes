package io.dongxi.page.panel

import io.dongxi.application.DongxiConfig
import io.dongxi.model.IAccessoryListContainer
import io.dongxi.model.IProductAccessoryListContainer
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ProductCategory.RING
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.util.ColorUtils.nattyBackgroundColor
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.Canvas
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.rect
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher

class RightPanel(
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
    textFieldStyler,
    linkStyler,
    focusManager,
    popups,
    modals,
    menuEventBus,
    baseProductSelectEventBus,
    accessorySelectEventBus
) {

    private val accessoryListContainer = when (pageType.productCategory) {
        NECKLACE -> getProductAccessoryListContainer()      // getProductAccessoryListContainer()   getBaseNecklacePendantsContainer()
        RING -> getProductAccessoryListContainer()          // getProductAccessoryListContainer()   getRingStonesContainer()
        else -> getDummyBaseProductsContainer()
    }

    init {
        size = Size(200, 200)
        children += listOf(accessoryListContainer)
        layout = constrain(accessoryListContainer, fill)
    }

    override fun render(canvas: Canvas) {
        // TODO define in app config, reference app config here.
        canvas.rect(bounds.atOrigin, nattyBackgroundColor())
    }

    override fun layoutForCurrentProductCategory() {
        // println("${panelInstanceName()} currentProductCategory: $currentProductCategory")
        relayout()
    }

    override fun layoutForCurrentBaseProductSelection() {
        println("${panelInstanceName()} currentBaseProduct: $currentBaseProduct")
        try {
            // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
            if (accessoryListContainer is IAccessoryListContainer) {
                accessoryListContainer.clearModel()
                accessoryListContainer.loadModel(currentBaseProduct.name ?: "A")
            }

            // Interesting... In Kotlin, I do not have to cast the object if I check 'object is interface' first.
            if (accessoryListContainer is IProductAccessoryListContainer) {
                accessoryListContainer.clearModel()
                accessoryListContainer.loadModel(currentBaseProduct.name ?: "A")
            }
        } catch (ex: Exception) {
            println("EXCEPTION ${panelInstanceName()} -> layoutForCurrentBaseProductSelection():  $ex")
        }

        accessoryListContainer.relayout()
        relayout()
    }


    override fun layoutForCurrentAccessorySelection() {
        println("${panelInstanceName()} currentAccessory: $currentAccessory")
        relayout()
    }

    override fun layoutForCompletedJewel() {
        // noop
    }
}
