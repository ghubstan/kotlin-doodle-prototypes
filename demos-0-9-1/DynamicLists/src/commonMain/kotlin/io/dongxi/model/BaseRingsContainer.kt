package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.RING
import io.dongxi.model.ScaledImage.SMALL_RING
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.BaseProductSelectEvent.SELECT_BASE_RING
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.list.DynamicList
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.panels.ScrollPanel
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.Color
import io.nacular.doodle.drawing.Color.Companion.Black
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


@Deprecated("Use BaseProductListContainer")
interface IBaseRingsContainer {
    val listCache: MutableMap<ProductCategory, DynamicList<Ring, SimpleMutableListModel<Ring>>>
    val list: DynamicList<Ring, SimpleMutableListModel<Ring>>
    val model: SimpleMutableListModel<Ring>
    fun build(): DynamicList<Ring, SimpleMutableListModel<Ring>>
}

@Deprecated("Use BaseProductListContainer")
class BaseRingsContainer(
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
) : IProductListContainer, IBaseRingsContainer, Container() {

    private val mainScope = MainScope() // The scope of BaseRingsContainer class, uses Dispatchers.Main.

    // This map is useless because every container is specific to product category.  Just cache the list -- no map.
    override val listCache = mutableMapOf<ProductCategory, DynamicList<Ring, SimpleMutableListModel<Ring>>>()

    override val model = SimpleMutableListModel<Ring>()
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
        foregroundColor = Color.Transparent
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
    override fun build(): DynamicList<Ring, SimpleMutableListModel<Ring>> {
        if (listCache.containsKey(RING)) {
            return listCache[RING]!!
        } else {
            val list = DynamicList(
                model,
                selectionModel = SingleItemSelectionModel(),
                itemVisualizer = BaseRingVisualizer(config),
                fitContent = setOf(Width, Height)
            ).apply {
                acceptsThemes = true // true when using inline behaviors, false when not using inline behaviors.
                cellAlignment = fill

                selectionChanged += { list, _, added ->
                    list[added.first()].getOrNull()?.let { selectedRing ->
                        println("BaseRingsContainer selected ring: ${selectedRing.name} file: ${selectedRing.file}")
                        mainScope.launch {
                            SELECT_BASE_RING.setBaseProductDetail(
                                selectedRing.name, selectedRing.file, selectedRing.image
                            )
                            baseProductSelectEventBus.produceEvent(SELECT_BASE_RING)
                            updateDebugLabelText(selectedRing)
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
            val rings = RingStoreMetadata.allSmallRings.sortedBy { it.first }.map { (name, path) ->
                Ring(name, path, mainScope.async { images.load(path)!! })
            }
            rings.forEach { model.add(it) }
            updateDebugLabelText(rings[0])
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

    private fun updateDebugLabelText(ring: Ring) {
        debugLabel.text = "Anel:  ${ring.name}  File:  ${ring.file}"
    }
}

@Deprecated("Use BaseProductListContainer")
class BaseRingListView(
    var ring: Ring,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(ring.name, Middle, Center).apply {
        fitText = setOf(Width, Height)
        styledText = StyledText(text, config.listFont, Black.paint)
    }

    private val photo = LazyImage(
        pendingImage = ring.image,
        canvasDestination = SMALL_RING.canvasDestination
    ).apply {
        toolTipText = ring.name
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

    fun update(ring: Ring, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new ring installed in it.
        this.ring = ring
        this.index = index
        this.selected = selected

        label.text = ring.name
        photo.pendingImage = ring.image
    }
}

// The Visualizer is designed to recycle view: reconfigure the view,
// to represent the new ring installed into it (the "infinite" list of items).
@Deprecated("Use BaseProductListContainer")
class BaseRingVisualizer(val config: DongxiConfig) : ItemVisualizer<Ring, IndexedItem> {
    override fun invoke(item: Ring, previous: View?, context: IndexedItem): View = when (previous) {
        is BaseRingListView -> previous.apply {
            update(
                ring = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> BaseRingListView(ring = item, index = context.index, selected = context.selected, config = config)
    }
}

