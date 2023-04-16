package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.NecklaceStoreMetadata.getLargeNecklaceMetadata
import io.dongxi.storage.PendantStoreMetadata
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.geometry.Rectangle
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
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

    private val necklacePhotoCanvasDestination = Rectangle(5, 15, 190, 190)
    private val necklacePhoto = LazyNecklacePendantPhotoView(necklace.image, necklacePhotoCanvasDestination)

    private val pendantPhotoCanvasDestination = Rectangle(5, 15, 43, 43)
    private val pendantPhoto = LazyNecklacePendantPhotoView(pendant.image, pendantPhotoCanvasDestination)


    init {
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(necklacePhoto, pendantPhoto)
        layout = constrain(necklacePhoto, pendantPhoto) { necklacePhotoBounds,
                                                          pendantPhotoBounds ->
            necklacePhotoBounds.top eq 10
            necklacePhotoBounds.left eq 10
            necklacePhotoBounds.width.preserve
            necklacePhotoBounds.height.preserve

            pendantPhotoBounds.left eq 83
            pendantPhotoBounds.centerY eq 188
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
    }
}