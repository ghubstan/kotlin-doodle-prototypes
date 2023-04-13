package io.dongxi.page.panel.event

import io.dongxi.model.AccessoryCategory.PENDANT
import io.dongxi.model.AccessoryCategory.STONE
import io.dongxi.model.SelectedAccessory
import io.nacular.doodle.image.Image
import kotlinx.coroutines.Deferred


// Not sure if this is a good idea: an enum with state.

enum class AccessorySelectEvent(val selectedAccessory: SelectedAccessory) {

    SELECT_STONE(SelectedAccessory(STONE, null, null, null)) {
        override fun setAccessoryDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedAccessory.name = name
            selectedAccessory.file = file
            selectedAccessory.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedAccessory.name != null &&
                    selectedAccessory.file != null &&
                    selectedAccessory.image != null
        }

        override fun accessoryDetail(): SelectedAccessory {
            return selectedAccessory
        }

        override fun clearAccessoryDetail() {
            selectedAccessory.name = null
            selectedAccessory.file = null
            selectedAccessory.image = null
        }
    },

    SELECT_PENDANT(SelectedAccessory(PENDANT, null, null, null)) {
        override fun setAccessoryDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedAccessory.name = name
            selectedAccessory.file = file
            selectedAccessory.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedAccessory.name != null &&
                    selectedAccessory.file != null &&
                    selectedAccessory.image != null
        }

        override fun accessoryDetail(): SelectedAccessory {
            return selectedAccessory
        }

        override fun clearAccessoryDetail() {
            selectedAccessory.name = null
            selectedAccessory.file = null
            selectedAccessory.image = null
        }
    };


    abstract fun setAccessoryDetail(
        name: String,
        file: String,
        image: Deferred<Image>?
    )

    abstract fun hasSelection(): Boolean

    abstract fun accessoryDetail(): SelectedAccessory

    abstract fun clearAccessoryDetail()

}