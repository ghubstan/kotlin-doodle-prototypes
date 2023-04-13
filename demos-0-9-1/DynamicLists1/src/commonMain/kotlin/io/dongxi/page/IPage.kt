package io.dongxi.page


interface IPage {
    val pageType: PageType
    fun description(): String
    fun destroy()
}