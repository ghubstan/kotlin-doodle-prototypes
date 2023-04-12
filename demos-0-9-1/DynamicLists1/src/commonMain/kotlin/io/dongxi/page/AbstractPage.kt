package io.dongxi.view

import io.nacular.doodle.core.View
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class AbstractPage : View() {

    val mainScope = MainScope() // The scope of AbstractPage class (and subclasses), uses Dispatchers.Main.

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }


}