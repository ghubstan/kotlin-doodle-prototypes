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
    // TODO migrate name,file,img -> IProduct
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
    // TODO migrate name,file,img -> IProductAccessory
    var name: String?,
    var file: String?,
    var image: Deferred<Image>?
) {
    fun isSet(): Boolean {
        return this.name != null && this.file != null
    }
}


/**
 * Data representing a base dummy product image.
 */


interface IProduct {
    val name: String
    val file: String
    val image: Deferred<Image>
    val productCategory: ProductCategory
}

interface IProductAccessory {
    val name: String
    val file: String
    val image: Deferred<Image>
    val accessoryCategory: AccessoryCategory
}

/**
 * Data representing a base dummy product image.
 */
@Serializable
data class DummyProduct(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProduct {
    override val productCategory: ProductCategory = ProductCategory.NONE
}

/**
 * Data representing a dummy product accessory image.
 */
@Serializable
data class DummyProductAccessory(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProductAccessory {
    override val accessoryCategory: AccessoryCategory = AccessoryCategory.NONE
}

/**
 * Data representing a base ring image.
 */
@Serializable
data class Ring(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProduct {
    override val productCategory: ProductCategory = ProductCategory.RING
}

/**
 * Data representing a ring-stone image.
 */
@Serializable
data class RingStone(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProductAccessory {
    override val accessoryCategory: AccessoryCategory = AccessoryCategory.STONE
}

/**
 * Data representing a base necklace image.
 */
@Serializable
data class Necklace(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProduct {
    override val productCategory: ProductCategory = ProductCategory.NECKLACE
}

/**
 * Data representing a necklace-pendants image.
 */
@Serializable
data class NecklacePendant(
    override val name: String,
    override val file: String,
    override val image: Deferred<Image>
) : IProductAccessory {
    override val accessoryCategory: AccessoryCategory = AccessoryCategory.PENDANT
}
