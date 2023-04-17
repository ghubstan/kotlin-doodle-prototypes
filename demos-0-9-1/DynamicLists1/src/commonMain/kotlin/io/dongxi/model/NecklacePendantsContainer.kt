package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.NECKLACE
import io.dongxi.model.ScaledImage.SMALL_NECKLACE_PENDANT
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEvent.SELECT_PENDANT
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.PendantStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.*
import io.nacular.doodle.controls.list.DynamicList
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.panels.ScrollPanel
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.core.View
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


interface INecklacePendantsContainer {
    val listCache: MutableMap<ProductCategory, DynamicList<NecklacePendant, SimpleMutableListModel<NecklacePendant>>>
    val list: DynamicList<NecklacePendant, SimpleMutableListModel<NecklacePendant>>
    val model: SimpleMutableListModel<NecklacePendant>
    fun build(productCategory: ProductCategory): DynamicList<NecklacePendant, SimpleMutableListModel<NecklacePendant>>
}

class NecklacePendantsContainer(
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
) : IAccessoryListContainer, INecklacePendantsContainer, Container() {

    private val mainScope = MainScope() // The scope of NecklacePendantsContainer class, uses Dispatchers.Main.

    // This cache is not useless, because each ring has its own set up compatible pendants.
    // TODO implement the map cache for selected base product (ring).
    override val listCache =
        mutableMapOf<ProductCategory, DynamicList<NecklacePendant, SimpleMutableListModel<NecklacePendant>>>()

    override val model = SimpleMutableListModel<NecklacePendant>()
    override val list = build(NECKLACE)

    private fun scrollPanel(content: View) = ScrollPanel(content).apply {
        contentWidthConstraints = { it eq width - verticalScrollBarWidth }
    }

    private val scrollableList = scrollPanel(list)

    init {
        clipCanvasToBounds = false
        size = Size(150, 200)
        loadModel("A")
        children += scrollableList
        layout = constrain(scrollableList, fill)

    }

    // TODO Remove from interface and make private?
    override fun build(productCategory: ProductCategory): DynamicList<NecklacePendant, SimpleMutableListModel<NecklacePendant>> {
        val list = DynamicList(
            model,
            selectionModel = SingleItemSelectionModel(),
            itemVisualizer = NecklacePendantVisualizer(config),
            fitContent = setOf(Width, Height)
        ).apply {
            acceptsThemes = true // true when using inline behaviors, false when not using inline behaviors.
            cellAlignment = fill

            selectionChanged += { list, _, added ->
                list[added.first()].getOrNull()?.let { selectedNecklacePendant ->
                    println("NecklacePendantsContainer selected pendant: ${selectedNecklacePendant.name} file: ${selectedNecklacePendant.file}")
                    mainScope.launch {
                        SELECT_PENDANT.setAccessoryDetail(
                            selectedNecklacePendant.name, selectedNecklacePendant.file, selectedNecklacePendant.image
                        )
                        accessorySelectEventBus.produceEvent(SELECT_PENDANT)
                    }
                }
            }

            setSelection(setOf(0))

        }
        return list
    }

    override fun loadModel(baseProductName: String) {
        mainScope.launch {
            PendantStoreMetadata.getPendants(baseProductName).sortedBy { it.first }.map { (name, path) ->
                model.add(NecklacePendant(name, path, mainScope.async { images.load(path)!! }))
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


class NecklacePendantListView(
    var pendant: NecklacePendant,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(pendant.name, Middle, Center).apply {
        fitText = setOf(Width, Height)
        styledText = StyledText(text, config.listFont, Black.paint)
    }

    private val photo = LazyNecklacePendantPhotoView(pendant.image, SMALL_NECKLACE_PENDANT.canvasDestination).apply {
        toolTipText = pendant.name
    }

    init {
        children += photo
        layout = constrain(photo, fill)
    }

    fun update(pendant: NecklacePendant, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new ring installed in it.
        this.pendant = pendant
        this.index = index
        this.selected = selected

        label.text = pendant.name
        photo.pendingImage = pendant.image
    }
}

// The Visualizer is designed to recycle view: reconfigure the view,
// to represent the new ring installed into it (the "infinite" list of items).
class NecklacePendantVisualizer(val config: DongxiConfig) : ItemVisualizer<NecklacePendant, IndexedItem> {
    override fun invoke(item: NecklacePendant, previous: View?, context: IndexedItem): View = when (previous) {
        is NecklacePendantListView -> previous.apply {
            update(
                pendant = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> NecklacePendantListView(
            pendant = item,
            index = context.index,
            selected = context.selected,
            config = config
        )
    }
}


