package io.dongxi.model.junk

import io.dongxi.model.IProduct
import io.dongxi.model.IProductAccessory
import io.dongxi.page.PageType
import kotlinx.coroutines.CoroutineScope

@Deprecated("Unused due to inheritance problems (in javascript?)")
// TODO Do not delete until I find out why using this fails to consistently
//      update the center panel's complete product images.
interface ICompleteProductContainer {
    val pageType: PageType
    val mainScope: CoroutineScope
    fun update(product: IProduct, accessory: IProductAccessory)
    fun destroy()
}