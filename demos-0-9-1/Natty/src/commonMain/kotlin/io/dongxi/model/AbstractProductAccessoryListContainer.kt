package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ProductCategory.*
import io.dongxi.model.ScaledImage.SMALL_NECKLACE_PENDANT
import io.dongxi.model.ScaledImage.SMALL_RING_STONE
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEvent
import io.dongxi.page.panel.event.AccessorySelectEvent.SELECT_PENDANT
import io.dongxi.page.panel.event.AccessorySelectEvent.SELECT_STONE
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.PendantStoreMetadata
import io.dongxi.storage.RingStoneStoreMetadata
import io.dongxi.util.ColorUtils.nattyFontColor
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
abstract class AbstractProductAccessoryListContainer(
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
    private val baseProductSelectEventBus: BaseProductSelectEventBus,
    private val accessorySelectEventBus: AccessorySelectEventBus
) : IProductAccessoryListContainer, Container() {

    override val mainScope = MainScope()

    val debugLabel = Label("Nenhum", Middle, Center).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Width)
        foregroundColor = Transparent
    }

    override val model: SimpleMutableListModel<IProductAccessory> = SimpleMutableListModel()
    override val list: DynamicList<IProductAccessory, SimpleMutableListModel<IProductAccessory>> =
        DynamicList(
            model,
            selectionModel = SingleItemSelectionModel(),
            itemVisualizer = ProductAccessoryVisualizer(pageType, config),
            fitContent = setOf(Width, Height)
        ).apply {
            acceptsThemes = true
            cellAlignment = fill
            selectionChanged += { list, _, added ->
                list[added.first()].getOrNull()?.let { accessory ->
                    println("Selected accessory name: ${accessory.name} file: ${accessory.file}")
                    mainScope.launch {
                        val event: AccessorySelectEvent = when (pageType.productCategory) {
                            BRACELET -> TODO()
                            EARRING -> TODO()
                            NECKLACE -> SELECT_PENDANT
                            RING -> SELECT_STONE
                            SCAPULAR -> TODO()
                            NONE -> TODO()
                        }
                        event.setAccessoryDetail(accessory.name, accessory.file, accessory.image)
                        accessorySelectEventBus.produceEvent(event)
                        updateDebugLabelText(accessory)
                    }
                }
            }
            setSelection(setOf(0))
        }

    private fun scrollPanel(content: View) = ScrollPanel(content).apply {
        contentWidthConstraints = { it eq width - verticalScrollBarWidth }
    }

    val scrollableList = scrollPanel(list)

    override fun loadModel(baseProductName: String) {
        mainScope.launch {
            when (pageType.productCategory) {
                NECKLACE -> loadNecklacePendants(baseProductName)
                RING -> loadRingStones(baseProductName)
                else -> {
                    // TODO
                }
            }
        }
    }

    private fun loadNecklacePendants(baseProductName: String) {
        val pendants = PendantStoreMetadata.getPendants(baseProductName).sortedBy { it.first }.map { (name, path) ->
            NecklacePendant(name, path, mainScope.async { images.load(path)!! })
        }
        pendants.forEach { model.add(it) }
        updateDebugLabelText(pendants[0])
    }

    private fun loadRingStones(baseProductName: String) {
        val stones = RingStoneStoreMetadata.getStones(baseProductName).sortedBy { it.first }.map { (name, path) ->
            RingStone(name, path, mainScope.async { images.load(path)!! })
        }
        stones.forEach { model.add(it) }
        updateDebugLabelText(stones[0])
    }

    override fun clearModel() {
        model.clear()
    }

    override fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }

    private fun updateDebugLabelText(accessory: IProductAccessory) {
        debugLabel.text = "${StringUtils.capitalizeWord(accessory.accessoryCategory.name)}:  ${accessory.name}"
    }
}


// The Visualizer is designed to recycle view: reconfigure the view, to represent
// the new accessory installed into it from the "infinite" list of items.
class ProductAccessoryVisualizer(val pageType: PageType, val config: DongxiConfig) :
    ItemVisualizer<IProductAccessory, IndexedItem> {
    override fun invoke(item: IProductAccessory, previous: View?, context: IndexedItem): View = when (previous) {
        is ProductAccessoryListView -> previous.apply {
            update(
                accessory = item,
                index = context.index,
                selected = context.selected
            )
        }

        else -> ProductAccessoryListView(
            pageType = pageType,
            accessory = item,
            index = context.index,
            selected = context.selected,
            config = config
        )
    }
}


class ProductAccessoryListView(
    val pageType: PageType,
    var accessory: IProductAccessory,
    var index: Int,
    var selected: Boolean,
    val config: DongxiConfig
) : View() {

    private val label = Label(accessory.name, Middle, Center).apply {
        fitText = setOf(Width, Height)
        // TODO define nattyFontColor() in app config, reference app config here.
        styledText = StyledText(text, config.listFont, nattyFontColor().paint)
    }

    private val photo = LazyImage(
        pendingImage = accessory.image,
        canvasDestination = when (pageType.productCategory) {
            BRACELET -> TODO()
            EARRING -> TODO()
            NECKLACE -> SMALL_NECKLACE_PENDANT.canvasDestination
            RING -> SMALL_RING_STONE.canvasDestination
            SCAPULAR -> TODO()
            NONE -> TODO()
        }
    ).apply {
        toolTipText = accessory.name
    }

    init {
        children += photo
        layout = constrain(photo, fill)
    }

    fun update(accessory: IProductAccessory, index: Int, selected: Boolean) {
        // Reconfigure the view to represent the new accessory installed in it.
        this.accessory = accessory
        this.index = index
        this.selected = selected

        label.text = accessory.name
        photo.pendingImage = accessory.image
    }
}
