package io.dongxi.model

import io.nacular.doodle.image.Image
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable


/**
 * Data representing a selected, updatable base product (item) selected from a list.
 */
@Serializable
data class SelectedBaseProduct(
    val productCategory: ProductCategory,
    var name: String?,
    var file: String?,
    var image: Deferred<Image>?
) {
    fun isSet(): Boolean {
        return this.name != null && this.file != null
    }
}

/**
 * Data representing a selected, updatable product accessory (item) selected from a list.
 */
@Serializable
data class SelectedAccessory(
    val accessoryCategory: AccessoryCategory,
    var name: String?,
    var file: String?,
    var image: Deferred<Image>?
) {
    fun isSet(): Boolean {
        return this.name != null && this.file != null
    }
}

/**
 * Data representing a ring image.
 */
@Serializable
data class Ring(val name: String, val file: String, val image: Deferred<Image>)

/**
 * Data representing a ring-stone image.
 */
@Serializable
data class RingStone(val name: String, val file: String, val image: Deferred<Image>)

/**
 * Data representing a large ring + accessory stone image.
 */
@Serializable
data class RingWithStone(val ring: Ring, val stone: RingStone)
