package io.dongxi.model

import io.dongxi.page.PageType
import io.nacular.doodle.controls.SimpleMutableListModel
import io.nacular.doodle.controls.list.DynamicList
import kotlinx.coroutines.CoroutineScope

interface IBaseProductListContainer {
    val pageType: PageType
    val mainScope: CoroutineScope
    val model: SimpleMutableListModel<IProduct>
    val list: DynamicList<IProduct, SimpleMutableListModel<IProduct>>
    fun loadModel()
    fun clearModel()
    fun destroy()
}
