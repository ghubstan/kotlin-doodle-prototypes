package io.dongxi.model

import io.dongxi.page.PageType
import io.nacular.doodle.controls.SimpleMutableListModel
import io.nacular.doodle.controls.list.DynamicList
import kotlinx.coroutines.CoroutineScope

interface IProductAccessoryListContainer {
    val pageType: PageType
    val mainScope: CoroutineScope
    val model: SimpleMutableListModel<IProductAccessory>
    val list: DynamicList<IProductAccessory, SimpleMutableListModel<IProductAccessory>>
    fun loadModel(baseProductName: String)
    fun clearModel()
    fun destroy()
}