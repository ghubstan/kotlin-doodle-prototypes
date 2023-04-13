package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.RING
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEvent.SELECT_STONE
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoneStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.list.DynamicList
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.layout.constraints.fill
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Height
import io.nacular.doodle.utils.Dimension.Width
import kotlinx.coroutines.*


interface IRingStonesContainer {
    val listCache: MutableMap<ProductCategory, DynamicList<RingStone, SimpleMutableListModel<RingStone>>>
    val list: DynamicList<RingStone, SimpleMutableListModel<RingStone>>
    val model: SimpleMutableListModel<RingStone>
    fun build(productCategory: ProductCategory): DynamicList<RingStone, SimpleMutableListModel<RingStone>>
}

class RingStonesContainer(
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
    private val baseProductSelectEventBus: BaseProductSelectEventBus,
    private val accessorySelectEventBus: AccessorySelectEventBus
) : IAccessoryListContainer, IRingStonesContainer, Container() {

    private val mainScope = MainScope() // The scope of RingStonesContainer class, uses Dispatchers.Main.

    // This cache is not useless, because each ring has its own set up compatible stones.
    // TODO implement the map cache for selected base product (ring).
    override val listCache = mutableMapOf<ProductCategory, DynamicList<RingStone, SimpleMutableListModel<RingStone>>>()

    override val model = SimpleMutableListModel<RingStone>()
    override val list = build(RING)

    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        loadModel()
        children += list
        layout = constrain(list, fill)
    }

    // TODO Remove from interface and make private?
    override fun build(productCategory: ProductCategory): DynamicList<RingStone, SimpleMutableListModel<RingStone>> {
        val list = DynamicList(
            model,
            selectionModel = SingleItemSelectionModel(),
            itemVisualizer = RingStoneVisualizer(config),
            fitContent = setOf(Width, Height)
        ).apply {
            acceptsThemes = true // true when using inline behaviors, false when not using inline behaviors.
            cellAlignment = fill

            selectionChanged += { list, _, added ->
                list[added.first()].getOrNull()?.let { selectedRingStone ->
                    println("RingStonesContainer selected stone: ${selectedRingStone.name} file: ${selectedRingStone.file}")
                    mainScope.launch {
                        SELECT_STONE.setAccessoryDetail(
                            selectedRingStone.name, selectedRingStone.file, selectedRingStone.image
                        )
                        accessorySelectEventBus.produceEvent(SELECT_STONE)
                    }
                }
            }
        }
        return list
    }

    override fun loadModel() {
        mainScope.launch {
            RingStoneStoreMetadata.getStones("A").sortedBy { it.first }.map { (name, path) ->
                model.add(RingStone(name, path, mainScope.async { images.load(path)!! }))
            }
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
}


class RingStoneListView(
    var stone: RingStone,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(stone.name)

    private val photo = LazyPhotoView(stone.image)

    init {
        children += label
        children += photo

        layout = constrain(label, photo) { labelBounds, photoBounds ->
            labelBounds.left eq 5
            labelBounds.centerY eq parent.centerY
            labelBounds.width eq 145
            labelBounds.height.preserve

            photoBounds.left eq labelBounds.right + 10
            photoBounds.centerY eq parent.centerY
            photoBounds.width.preserve
            photoBounds.height.preserve
            /*
            labelBounds.left eq 5
            labelBounds.centerY eq parent.centerY
            labelBounds.width.preserve
            labelBounds.height.preserve

            photoBounds.left eq labelBounds.right + 20
            photoBounds.centerY eq parent.centerY
            photoBounds.width.preserve
            photoBounds.height.preserve
             */
        }
    }

    fun update(stone: RingStone, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new ring installed in it.
        this.stone = stone
        this.index = index
        this.selected = selected

        label.text = stone.name
        photo.pendingImage = stone.image
    }
}

// The Visualizer is designed to recycle view: reconfigure the view,
// to represent the new ring installed into it (the "infinite" list of items).
class RingStoneVisualizer(val config: DongxiConfig) : ItemVisualizer<RingStone, IndexedItem> {
    override fun invoke(item: RingStone, previous: View?, context: IndexedItem): View = when (previous) {
        is RingStoneListView -> previous.apply {
            update(
                stone = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> RingStoneListView(stone = item, index = context.index, selected = context.selected, config = config)
    }
}

