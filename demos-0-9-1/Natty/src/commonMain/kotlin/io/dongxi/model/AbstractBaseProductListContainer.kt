package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.*
import io.dongxi.model.ScaledImage.SMALL_NECKLACE
import io.dongxi.model.ScaledImage.SMALL_RING
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.BaseProductSelectEvent
import io.dongxi.page.panel.event.BaseProductSelectEvent.*
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata.allSmallNecklaces
import io.dongxi.storage.RingStoreMetadata.allSmallRings
import io.dongxi.util.StringUtils
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.list.DynamicList
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.panels.ScrollPanel
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.*


@Suppress("unused")
abstract class AbstractBaseProductListContainer(
    final override val pageType: PageType,
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
    private val modals: ModalManager,
    private val menuEventBus: MenuEventBus,
    private val baseProductSelectEventBus: BaseProductSelectEventBus
) : IBaseProductListContainer, Container() {

    override val mainScope = MainScope()

    val debugLabel = Label("Nenhum", Middle, Center).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    final override val model: SimpleMutableListModel<IProduct> = SimpleMutableListModel()
    override val list: DynamicList<IProduct, SimpleMutableListModel<IProduct>> =
        DynamicList(
            model,
            selectionModel = SingleItemSelectionModel(),
            itemVisualizer = BaseProductVisualizer(pageType, config),
            fitContent = setOf(Width, Height)
        ).apply {
            acceptsThemes = true
            cellAlignment = fill
            selectionChanged += { list, _, added ->
                list[added.first()].getOrNull()?.let { baseProduct ->
                    println("Selected baseProduct name: ${baseProduct.name} file: ${baseProduct.file}")
                    mainScope.launch {
                        val event: BaseProductSelectEvent = when (pageType.productCategory) {
                            BRACELET -> SELECT_BASE_BRACELET
                            EARRING -> SELECT_BASE_EARRING
                            NECKLACE -> SELECT_BASE_NECKLACE
                            RING -> SELECT_BASE_RING
                            SCAPULAR -> SELECT_BASE_SCAPULAR
                            NONE -> TODO()
                        }
                        event.setBaseProductDetail(baseProduct.name, baseProduct.file, baseProduct.image)
                        baseProductSelectEventBus.produceEvent(event)
                        updateDebugLabelText(baseProduct)
                    }
                }
            }
            setSelection(setOf(0))
        }


    private fun scrollPanel(content: View) = ScrollPanel(content).apply {
        contentWidthConstraints = { it eq width - verticalScrollBarWidth }
    }

    val scrollableList = scrollPanel(list)

    override fun loadModel() {
        mainScope.launch {
            when (pageType.productCategory) {
                NECKLACE -> loadNecklaces()
                RING -> loadRings()
                else -> {
                    // TODO
                }
            }
        }
    }

    private fun loadNecklaces() {
        val necklaces = allSmallNecklaces.sortedBy { it.first }.map { (name, path) ->
            Necklace(name, path, mainScope.async { images.load(path)!! })
        }
        necklaces.forEach { model.add(it) }
        updateDebugLabelText(necklaces[0])
    }

    private fun loadRings() {
        val rings = allSmallRings.sortedBy { it.first }.map { (name, path) ->
            Ring(name, path, mainScope.async { images.load(path)!! })
        }
        rings.forEach { model.add(it) }
        updateDebugLabelText(rings[0])
    }

    override fun clearModel() {
        model.clear()
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    private fun updateDebugLabelText(baseProduct: IProduct) {
        debugLabel.text = "${StringUtils.capitalizeWord(baseProduct.productCategory.name)}:  ${baseProduct.name}"
    }
}


// The Visualizer is designed to recycle view: reconfigure the view, to represent
// the new base product installed into it from the "infinite" list of items.
class BaseProductVisualizer(val pageType: PageType, val config: DongxiConfig) : ItemVisualizer<IProduct, IndexedItem> {
    override fun invoke(item: IProduct, previous: View?, context: IndexedItem): View = when (previous) {
        is BaseProductListView -> previous.apply {
            update(
                baseProduct = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> BaseProductListView(
            pageType = pageType,
            baseProduct = item,
            index = context.index,
            selected = context.selected,
            config = config
        )
    }
}

class BaseProductListView(
    val pageType: PageType,
    var baseProduct: IProduct,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(
        styledText = styledLabelText(baseProduct.name),
        verticalAlignment = Middle,
        horizontalAlignment = Center
    ).apply {
        fitText = setOf(Width, Height)
    }

    private val photo = LazyImage(
        pendingImage = baseProduct.image,
        canvasDestination = when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> SMALL_NECKLACE.canvasDestination
            RING -> SMALL_RING.canvasDestination
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    ).apply {
        toolTipText = baseProduct.name
    }

    init {
        children += label
        children += photo

        layout = constrain(label, photo) { labelBounds, photoBounds ->
            labelBounds.left eq 5
            labelBounds.centerY eq parent.centerY
            labelBounds.width.preserve
            labelBounds.height.preserve

            photoBounds.left eq labelBounds.right + 5
            photoBounds.centerY eq parent.centerY
            photoBounds.width.preserve
            photoBounds.height.preserve
        }
    }

    fun update(baseProduct: IProduct, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new base product installed in it.
        this.baseProduct = baseProduct
        this.index = index
        this.selected = selected

        label.styledText = styledLabelText(baseProduct.name)
        photo.pendingImage = baseProduct.image
    }

    private fun styledLabelText(text: String): StyledText {
        return StyledText(
            text = text,
            font = config.listFont,
            foreground = config.listFontColor.paint
        )
    }
}
