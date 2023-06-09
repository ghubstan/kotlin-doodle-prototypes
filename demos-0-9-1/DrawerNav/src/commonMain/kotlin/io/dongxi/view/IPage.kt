package io.dongxi.view

import io.dongxi.application.DongxiConfig
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher


interface IPage {
    val config: DongxiConfig
    val uiDispatcher: CoroutineDispatcher
    val animator: Animator
    val pathMetrics: PathMetrics
    val fonts: FontLoader
    val theme: DynamicTheme
    val themes: ThemeManager
    val images: ImageLoader
    val textMetrics: TextMetrics
    val linkStyler: NativeHyperLinkStyler
    val focusManager: FocusManager
    val popups: PopupManager
    val modals: ModalManager
    fun description(): String
    fun shutdown()
}