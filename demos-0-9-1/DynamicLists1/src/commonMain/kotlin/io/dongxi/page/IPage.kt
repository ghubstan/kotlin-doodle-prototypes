package io.dongxi.page


interface IPage {
    val pageTitle: String
    fun description(): String
    fun destroy()
}