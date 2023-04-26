package io.dongxi.page.panel

import io.dongxi.page.PageType

interface IPanel {
    val pageType: PageType
    fun destroy()
}