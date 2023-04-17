package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ScaledImage.SMALL_NECKLACE
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.BaseProductSelectEvent.SELECT_BASE_NECKLACE
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.list.DynamicList
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.panels.ScrollPanel
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Color.Companion.Black
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.drawing.paint
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
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


interface IBaseNecklacesContainer {
    val listCache: MutableMap<ProductCategory, DynamicList<Necklace, SimpleMutableListModel<Necklace>>>
    val list: DynamicList<Necklace, SimpleMutableListModel<Necklace>>
    val model: SimpleMutableListModel<Necklace>
    fun build(): DynamicList<Necklace, SimpleMutableListModel<Necklace>>
}

class BaseNecklacesContainer(
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
) : IProductListContainer, IBaseNecklacesContainer, Container() {

    private val mainScope = MainScope() // The scope of BaseNecklacesContainer class, uses Dispatchers.Main.

    // This map is useless because every container is specific to product category.  Just cache the list -- no map.
    override val listCache = mutableMapOf<ProductCategory, DynamicList<Necklace, SimpleMutableListModel<Necklace>>>()

    override val model = SimpleMutableListModel<Necklace>()
    override val list = build()

    private fun scrollPanel(content: View) = ScrollPanel(content).apply {
        contentWidthConstraints = { it eq width - verticalScrollBarWidth }
    }

    private val scrollableList = scrollPanel(list)

    private val debugLabel = Label(
        "NADA",
        Middle,
        Center
    ).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        loadModel()
        children += debugLabel
        children += scrollableList
        layout = constrain(debugLabel, scrollableList) { debugLabelBounds, listBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            listBounds.top eq debugLabelBounds.bottom + 5
            listBounds.left eq 5
            listBounds.width eq parent.width
            listBounds.bottom eq parent.bottom - 5
        }
    }

    // TODO Remove from interface and make private?
    override fun build(): DynamicList<Necklace, SimpleMutableListModel<Necklace>> {
        if (listCache.containsKey(NECKLACE)) {
            return listCache[NECKLACE]!!
        } else {
            val list = DynamicList(
                model,
                selectionModel = SingleItemSelectionModel(),
                itemVisualizer = BaseNecklaceVisualizer(config),
                fitContent = setOf(Width, Height)
            ).apply {
                acceptsThemes = true // true when using inline behaviors, false when not using inline behaviors.
                cellAlignment = fill

                selectionChanged += { list, _, added ->
                    list[added.first()].getOrNull()?.let { selectedNecklace ->
                        println("BaseNecklacesContainer selected necklace: ${selectedNecklace.name} file: ${selectedNecklace.file}")
                        mainScope.launch {
                            SELECT_BASE_NECKLACE.setBaseProductDetail(
                                selectedNecklace.name, selectedNecklace.file, selectedNecklace.image
                            )
                            baseProductSelectEventBus.produceEvent(SELECT_BASE_NECKLACE)
                            updateDebugLabelText(selectedNecklace)
                        }
                    }
                }
                setSelection(setOf(0))
            }
            return list
        }
    }

    override fun loadModel() {
        mainScope.launch {
            val necklaces = NecklaceStoreMetadata.allSmallNecklaces.sortedBy { it.first }.map { (name, path) ->
                Necklace(name, path, mainScope.async { images.load(path)!! })
            }
            necklaces.forEach { model.add(it) }
            updateDebugLabelText(necklaces[0])
        }
    }

    override fun clearModel() {
        model.clear()
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    private fun updateDebugLabelText(necklace: Necklace) {
        debugLabel.text = "Color:  ${necklace.name}  File:  ${necklace.file}"
    }
}

class BaseNecklaceListView(
    var necklace: Necklace,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(necklace.name, Middle, Center).apply {
        fitText = setOf(Width, Height)
        styledText = StyledText(text, config.listFont, Black.paint)
    }

    private val photo = LazyImage(
        pendingImage = necklace.image,
        canvasDestination = SMALL_NECKLACE.canvasDestination
    ).apply {
        toolTipText = necklace.name
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

    fun update(necklace: Necklace, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new necklace installed in it.
        this.necklace = necklace
        this.index = index
        this.selected = selected

        label.text = necklace.name
        photo.pendingImage = necklace.image
    }
}

// The Visualizer is designed to recycle view: reconfigure the view,
// to represent the new necklace installed into it (the "infinite" list of items).
class BaseNecklaceVisualizer(val config: DongxiConfig) : ItemVisualizer<Necklace, IndexedItem> {
    override fun invoke(item: Necklace, previous: View?, context: IndexedItem): View = when (previous) {
        is BaseNecklaceListView -> previous.apply {
            update(
                necklace = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> BaseNecklaceListView(
            necklace = item,
            index = context.index,
            selected = context.selected,
            config = config
        )
    }
}


