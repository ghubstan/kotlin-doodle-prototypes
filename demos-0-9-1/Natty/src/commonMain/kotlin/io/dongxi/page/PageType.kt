package io.dongxi.page

import io.dongxi.model.ProductCategory
import io.dongxi.model.ProductCategory.*

enum class PageType(val pageTitle: String, val productCategory: ProductCategory) {
    HOME("Casa", NONE),
    RINGS("Aneis", RING),
    NECKLACES("Colares", NECKLACE),
    SCAPULARS("Escapulários", SCAPULAR),
    BRACELETS("Pulseiras", BRACELET),
    EAR_RINGS("Brincos", EARRING),
    ABOUT("Sobre", NONE)
}
