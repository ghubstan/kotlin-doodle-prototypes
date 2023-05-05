package io.dongxi.page.panel.form.control

import io.dongxi.application.DongxiConfig
import io.dongxi.page.MenuEventBus
import io.dongxi.page.PageType
import io.dongxi.page.panel.event.AccessorySelectEventBus
import io.dongxi.page.panel.event.BaseProductSelectEventBus
import io.dongxi.page.panel.form.control.ControlType.*
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
import io.nacular.doodle.theme.native.NativeTextFieldStyler
import kotlinx.coroutines.CoroutineDispatcher
import org.kodein.di.DI
import org.kodein.di.instance

interface IFormControlFactory {
    fun buildControl(controlType: ControlType): IControl
    val controlCache: MutableMap<ControlType, IControl>
}

class FormControlFactory(
    private val pageType: PageType,
    private val config: DongxiConfig,
    val commonDI: DI
) : IFormControlFactory {

    private val animator: Animator by commonDI.instance<Animator>()
    private val focusManager: FocusManager by commonDI.instance<FocusManager>()
    private val fonts: FontLoader by commonDI.instance<FontLoader>()
    private val images: ImageLoader by commonDI.instance<ImageLoader>()
    private val linkStyler: NativeHyperLinkStyler by commonDI.instance<NativeHyperLinkStyler>()
    private val modals: ModalManager by commonDI.instance<ModalManager>()
    private val pathMetrics: PathMetrics by commonDI.instance<PathMetrics>()
    private val popups: PopupManager by commonDI.instance<PopupManager>()
    private val textFieldStyler: NativeTextFieldStyler by commonDI.instance<NativeTextFieldStyler>()
    private val textMetrics: TextMetrics by commonDI.instance<TextMetrics>()
    private val theme: DynamicTheme by commonDI.instance<DynamicTheme>()
    private val themes: ThemeManager by commonDI.instance<ThemeManager>()
    private val uiDispatcher: CoroutineDispatcher by commonDI.instance<CoroutineDispatcher>()

    val menuEventBus: MenuEventBus by commonDI.instance<MenuEventBus>()
    val baseProductSelectEventBus: BaseProductSelectEventBus by commonDI.instance<BaseProductSelectEventBus>()
    val accessorySelectEventBus: AccessorySelectEventBus by commonDI.instance<AccessorySelectEventBus>()

    override val controlCache = mutableMapOf<ControlType, IControl>()

    override fun buildControl(controlType: ControlType): IControl {
        return if (controlCache.containsKey(controlType)) {
            controlCache[controlType]!!
        } else {
            val control = when (controlType) {
                CPF -> cpfControl()
                CHANGE_PASSWORD, SET_PASSWORD -> userPasswordControl()
            }
            controlCache[controlType] = control
            control
        }
    }

    private fun cpfControl(): CpfControl {
        return CpfControl(pageType, config, commonDI)
    }

    private fun userPasswordControl(): UserPasswordControl {
        return UserPasswordControl(pageType, config, commonDI)
    }
}