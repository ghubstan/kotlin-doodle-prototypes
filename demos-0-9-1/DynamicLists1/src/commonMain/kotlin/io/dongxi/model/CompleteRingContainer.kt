package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ScaledImage.LARGE_RING
import io.dongxi.model.ScaledImage.LARGE_RING_STONE
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoneStoreMetadata.getStones
import io.dongxi.storage.RingStoreMetadata.getLargeRingMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Color.Companion.Transparent
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import io.nacular.doodle.utils.Dimension.Width
import io.nacular.doodle.utils.HorizontalAlignment.Center
import io.nacular.doodle.utils.VerticalAlignment.Middle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async


interface ICompleteRingContainer {
    fun update(ring: Ring, stone: RingStone)
}


class CompleteRingContainer(
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
) : ICompleteRingContainer, Container() {

    private val mainScope = MainScope() // The scope of RingWithStoneContainer class, uses Dispatchers.Main.

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

    private val defaultRingMetadata = getLargeRingMetadata("A")
    private var defaultStoneMetadata: Pair<String, String> = getStones(defaultRingMetadata.first)[0]

    private var ring: Ring = Ring(
        defaultRingMetadata.first,
        defaultRingMetadata.second,
        mainScope.async { images.load(defaultRingMetadata.second)!! })
    private var stone: RingStone = RingStone(
        defaultStoneMetadata.first,
        defaultStoneMetadata.second,
        mainScope.async { images.load(defaultStoneMetadata.second)!! })


    private val ringPhoto = LazyImage(
        pendingImage = ring.image,
        canvasDestination = LARGE_RING.canvasDestination
    )

    private val stonePhoto = LazyImage(
        pendingImage = stone.image,
        canvasDestination = LARGE_RING_STONE.canvasDestination
    )

    init {
        updateDebugLabelText()
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(debugLabel, ringPhoto, stonePhoto)
        layout = constrain(debugLabel, ringPhoto, stonePhoto) { debugLabelBounds,
                                                                ringPhotoBounds,
                                                                stonePhotoBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            ringPhotoBounds.top eq debugLabelBounds.bottom + 10
            ringPhotoBounds.left eq 10
            ringPhotoBounds.width.preserve
            ringPhotoBounds.height.preserve

            stonePhotoBounds.left eq 50
            stonePhotoBounds.centerY eq 122
            stonePhotoBounds.width.preserve
            stonePhotoBounds.height.preserve
        }
    }

    override fun update(ring: Ring, stone: RingStone) {
        // Reconfigure the view to represent the new ring+stone installed in it.
        this.ring = ring
        this.stone = stone

        ringPhoto.pendingImage = ring.image
        stonePhoto.pendingImage = stone.image

        updateDebugLabelText()
    }


    private fun updateDebugLabelText() {
        debugLabel.text = "Anel:  ${ring.name}  Pedra:  ${stone.name}"
    }
}