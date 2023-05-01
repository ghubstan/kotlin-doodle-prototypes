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

interface IFormControlFactory {
    fun buildControl(controlType: ControlType): IControl
    val controlCache: MutableMap<ControlType, IControl>
}

class FormControlFactory(
    private val pageType: PageType,
    private val config: DongxiConfig,
    private val uiDispatcher: CoroutineDispatcher,
    private val animator: Animator,
    private val pathMetrics: PathMetrics,
    private val fonts: FontLoader,
    private val theme: DynamicTheme,
    private val themes: ThemeManager,
    private val images: ImageLoader,
    private val textMetrics: TextMetrics,
    private val textFieldStyler: NativeTextFieldStyler,
    private val linkStyler: NativeHyperLinkStyler,
    private val focusManager: FocusManager,
    private val popups: PopupManager,
    private val modals: ModalManager,
    private val menuEventBus: MenuEventBus,
    private val baseProductSelectEventBus: BaseProductSelectEventBus,
    private val accessorySelectEventBus: AccessorySelectEventBus
) : IFormControlFactory {

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
        return CpfControl(
            pageType,
            config,
            uiDispatcher,
            animator,
            pathMetrics,
            fonts,
            theme,
            themes,
            images,
            textMetrics,
            textFieldStyler,
            linkStyler,
            focusManager,
            popups,
            modals,
            menuEventBus,
            baseProductSelectEventBus,
            accessorySelectEventBus
        )
    }

    private fun userPasswordControl(): UserPasswordControl {
        return UserPasswordControl(
            pageType,
            config,
            uiDispatcher,
            animator,
            pathMetrics,
            fonts,
            theme,
            themes,
            images,
            textMetrics,
            textFieldStyler,
            linkStyler,
            focusManager,
            popups,
            modals,
            menuEventBus,
            baseProductSelectEventBus,
            accessorySelectEventBus
        )
    }
}