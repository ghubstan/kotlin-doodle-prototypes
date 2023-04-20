package io.dongxi.model

import io.dongxi.page.PageType
import kotlinx.coroutines.CoroutineScope

interface ICompleteProductContainer {
    val pageType: PageType
    val mainScope: CoroutineScope
    fun update(product: IProduct, accessory: IProductAccessory)
    fun destroy()
}