package io.dongxi.page.panel.form


import io.nacular.doodle.controls.buttons.PushButton
import io.nacular.doodle.controls.form.Form
import io.nacular.doodle.controls.form.LabeledConfig
import io.nacular.doodle.controls.form.TextFieldConfig
import io.nacular.doodle.controls.form.form
import io.nacular.doodle.controls.form.labeled
import io.nacular.doodle.controls.form.radioList
import io.nacular.doodle.controls.form.textField
import io.nacular.doodle.controls.form.verticalLayout
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.Font
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.text.invoke
import io.nacular.doodle.utils.ToStringIntEncoder
import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.nacular.doodle.animation.Animator
import io.nacular.doodle.controls.PopupManager
import io.nacular.doodle.controls.modal.ModalManager
import io.nacular.doodle.core.Container
import io.nacular.doodle.drawing.FontLoader
import io.nacular.doodle.drawing.TextMetrics
import io.nacular.doodle.focus.FocusManager
import io.nacular.doodle.geometry.PathMetrics
import io.nacular.doodle.image.ImageLoader
import io.nacular.doodle.theme.ThemeManager
import io.nacular.doodle.theme.adhoc.DynamicTheme
import io.nacular.doodle.theme.native.NativeHyperLinkStyler
import kotlinx.coroutines.CoroutineDispatcher

abstract class AbstractForm(
    val pageType: PageType,
    val config: DongxiConfig,
    val uiDispatcher: CoroutineDispatcher,
    val animator: Animator,
    val pathMetrics: PathMetrics,
    val fonts: FontLoader,
    val theme: DynamicTheme,
    val themes: ThemeManager,
    val images: ImageLoader,
    val textMetrics: TextMetrics,
    val linkStyler: NativeHyperLinkStyler,
    val focusManager: FocusManager,
    val popups: PopupManager,
    val modals: ModalManager,
    val menuEventBus: MenuEventBus,
    val baseProductSelectEventBus: BaseProductSelectEventBus,
    val accessorySelectEventBus: AccessorySelectEventBus
) : Container() {

}