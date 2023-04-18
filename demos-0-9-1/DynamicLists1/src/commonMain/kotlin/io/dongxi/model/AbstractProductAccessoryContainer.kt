package io.dongxi.model

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class AbstractProductAccessoryContainer {

    val mainScope = MainScope() // The scope of AbstractProductContainer (this) class, uses Dispatchers.Main.

    fun destroy() {
        // Cancels all coroutines launched in this scope.
        mainScope.cancel()
        // cleanup here
    }
}