package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.model.ScaledImage.LARGE_NECKLACE
import io.dongxi.model.ScaledImage.LARGE_NECKLACE_PENDANT
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata.getLargeNecklaceMetadata
import io.dongxi.storage.PendantStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.Color
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
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.HorizontalAlignment
import io.nacular.doodle.utils.VerticalAlignment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async


interface ICompleteNecklaceContainer {
    fun update(necklace: Necklace, pendant: NecklacePendant)
}


class CompleteNecklaceContainer(
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
) : ICompleteNecklaceContainer, Container() {

    private val mainScope = MainScope() // The scope of NecklaceWithStoneContainer class, uses Dispatchers.Main.

    private val debugLabel = Label(
        "NADA",
        VerticalAlignment.Middle,
        HorizontalAlignment.Center
    ).apply {
        font = config.panelDebugFont
        height = 24.0
        fitText = setOf(Dimension.Width)
        foregroundColor = Color.Transparent
    }

    private val defaultNecklaceMetadata = getLargeNecklaceMetadata("A")
    private var defaultPendantMetadata: Pair<String, String> =
        PendantStoreMetadata.getPendants(defaultNecklaceMetadata.first)[0]

    private var necklace: Necklace = Necklace(
        defaultNecklaceMetadata.first,
        defaultNecklaceMetadata.second,
        mainScope.async { images.load(defaultNecklaceMetadata.second)!! })
    private var pendant: NecklacePendant = NecklacePendant(
        defaultPendantMetadata.first,
        defaultPendantMetadata.second,
        mainScope.async { images.load(defaultPendantMetadata.second)!! })


    private val necklacePhoto = LazyImage(
        pendingImage = necklace.image,
        canvasDestination = LARGE_NECKLACE.canvasDestination
    )

    private val pendantPhoto = LazyImage(
        pendingImage = pendant.image,
        canvasDestination = LARGE_NECKLACE_PENDANT.canvasDestination
    )

    init {
        updateDebugLabelText()
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(debugLabel, necklacePhoto, pendantPhoto)
        layout = constrain(debugLabel, necklacePhoto, pendantPhoto) { debugLabelBounds,
                                                                      necklacePhotoBounds,
                                                                      pendantPhotoBounds ->
            debugLabelBounds.top eq 5
            debugLabelBounds.left eq 5
            debugLabelBounds.width.preserve
            debugLabelBounds.height.preserve

            necklacePhotoBounds.top eq debugLabelBounds.bottom + 10
            necklacePhotoBounds.left eq 10
            necklacePhotoBounds.width.preserve
            necklacePhotoBounds.height.preserve

            pendantPhotoBounds.left eq 83
            pendantPhotoBounds.centerY eq 217
            pendantPhotoBounds.width.preserve
            pendantPhotoBounds.height.preserve
        }
    }

    override fun update(necklace: Necklace, pendant: NecklacePendant) {
        // Reconfigure the view to represent the new necklace-pendant installed in it.
        this.necklace = necklace
        this.pendant = pendant

        necklacePhoto.pendingImage = necklace.image
        pendantPhoto.pendingImage = pendant.image

        updateDebugLabelText()
    }

    private fun updateDebugLabelText() {
        debugLabel.text = "Colar:  ${necklace.name}  Pendant:  ${pendant.name}"
    }
}