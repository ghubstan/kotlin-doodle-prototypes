package io.dongxi.page

import io.dongxi.model.ProductCategory
import io.dongxi.model.ProductCategory.*

enum class PageType(val pageTitle: String, val productCategory: ProductCategory) {

    // Product Page Types
    
    RINGS("Aneis", RING),
    NECKLACES("Colares", NECKLACE),
    SCAPULARS("Escapulários", SCAPULAR),
    BRACELETS("Pulseiras", BRACELET),
    EAR_RINGS("Brincos", EARRING),

    // Non Product Page Types

    ABOUT("Sobre", NONE),
    BASKET("Carrihno de Compras", NONE),
    HOME("Home", NONE),
    LOGIN("Entre", NONE),
    LOGOUT("Sair", NONE),
    PAYMENT("Pagamento", NONE),
    REGISTER("Crie a Sua Conta", NONE)
}
