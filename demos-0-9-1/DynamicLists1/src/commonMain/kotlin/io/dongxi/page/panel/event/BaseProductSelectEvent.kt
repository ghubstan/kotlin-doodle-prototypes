package io.dongxi.page.panel.event

import io.dongxi.model.ProductCategory.*
import io.dongxi.model.SelectedBaseProduct
import io.nacular.doodle.image.Image
import kotlinx.coroutines.Deferred

// Not sure if this is a good idea: an enum with state.

enum class BaseProductSelectEvent(val selectedBaseProduct: SelectedBaseProduct) {

    SELECT_BASE_BRACELET(SelectedBaseProduct(BRACELET, null, null, null)) {
        override fun setBaseProductDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedBaseProduct.name = name
            selectedBaseProduct.file = file
            selectedBaseProduct.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedBaseProduct.name != null &&
                    selectedBaseProduct.file != null &&
                    selectedBaseProduct.image != null
        }

        override fun baseProductDetail(): SelectedBaseProduct {
            return selectedBaseProduct
        }

        override fun clearBaseProductDetail() {
            selectedBaseProduct.name = null
            selectedBaseProduct.file = null
            selectedBaseProduct.image = null
        }
    },
    SELECT_BASE_EARRING(SelectedBaseProduct(EARRING, null, null, null)) {
        override fun setBaseProductDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedBaseProduct.name = name
            selectedBaseProduct.file = file
            selectedBaseProduct.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedBaseProduct.name != null &&
                    selectedBaseProduct.file != null &&
                    selectedBaseProduct.image != null
        }

        override fun baseProductDetail(): SelectedBaseProduct {
            return selectedBaseProduct
        }

        override fun clearBaseProductDetail() {
            selectedBaseProduct.name = null
            selectedBaseProduct.file = null
            selectedBaseProduct.image = null
        }
    },
    SELECT_BASE_NECKLACE(SelectedBaseProduct(NECKLACE, null, null, null)) {
        override fun setBaseProductDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedBaseProduct.name = name
            selectedBaseProduct.file = file
            selectedBaseProduct.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedBaseProduct.name != null &&
                    selectedBaseProduct.file != null &&
                    selectedBaseProduct.image != null
        }

        override fun baseProductDetail(): SelectedBaseProduct {
            return selectedBaseProduct
        }

        override fun clearBaseProductDetail() {
            selectedBaseProduct.name = null
            selectedBaseProduct.file = null
            selectedBaseProduct.image = null
        }
    },
    SELECT_BASE_RING(SelectedBaseProduct(RING, null, null, null)) {
        override fun setBaseProductDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedBaseProduct.name = name
            selectedBaseProduct.file = file
            selectedBaseProduct.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedBaseProduct.name != null &&
                    selectedBaseProduct.file != null &&
                    selectedBaseProduct.image != null
        }

        override fun baseProductDetail(): SelectedBaseProduct {
            return selectedBaseProduct
        }

        override fun clearBaseProductDetail() {
            selectedBaseProduct.name = null
            selectedBaseProduct.file = null
            selectedBaseProduct.image = null
        }
    },
    SELECT_BASE_SCAPULAR(SelectedBaseProduct(SCAPULAR, null, null, null)) {
        override fun setBaseProductDetail(name: String, file: String, image: Deferred<Image>?) {
            selectedBaseProduct.name = name
            selectedBaseProduct.file = file
            selectedBaseProduct.image = image
        }

        override fun hasSelection(): Boolean {
            return selectedBaseProduct.name != null &&
                    selectedBaseProduct.file != null &&
                    selectedBaseProduct.image != null
        }
        
        override fun baseProductDetail(): SelectedBaseProduct {
            return selectedBaseProduct
        }

        override fun clearBaseProductDetail() {
            selectedBaseProduct.name = null
            selectedBaseProduct.file = null
            selectedBaseProduct.image = null
        }
    };

    abstract fun setBaseProductDetail(
        name: String,
        file: String,
        image: Deferred<Image>?
    )

    abstract fun hasSelection(): Boolean

    abstract fun baseProductDetail(): SelectedBaseProduct

    abstract fun clearBaseProductDetail()
}
