package io.dongxi.model

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.storage.RingStoneStoreMetadata.getStones
import io.dongxi.storage.RingStoreMetadata.getLargeRingMetadata
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

    private val ringPhotoCanvasDestination = Rectangle(5, 15, 150, 150)
    private val ringPhoto = LazyCompleteRingPhotoView(ring.image, ringPhotoCanvasDestination)

    private val stonePhotoCanvasDestination = Rectangle(5, 15, 30, 30)
    private val stonePhoto = LazyCompleteRingPhotoView(stone.image, stonePhotoCanvasDestination)


    init {
        clipCanvasToBounds = false
        size = Size(200, 200)

        children += listOf(ringPhoto, stonePhoto)
        layout = constrain(ringPhoto, stonePhoto) { ringPhotoBounds,
                                                    stonePhotoBounds ->
            ringPhotoBounds.top eq 10
            ringPhotoBounds.left eq 10
            ringPhotoBounds.width.preserve
            ringPhotoBounds.height.preserve

            stonePhotoBounds.left eq ringPhotoBounds.centerX - 120
            stonePhotoBounds.centerY eq ringPhotoBounds.centerY - 75
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
    }

}