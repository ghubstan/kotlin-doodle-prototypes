package io.dongxi.page


import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class MenuEvent {

    // Non-Product Pages
    GO_HOME,
    GO_ABOUT,
    GO_LOGIN,
    GO_LOGOUT,
    GO_REGISTER,
    GO_BASKET,
    GO_PAYMENT,

    // Product Pages
    GO_RINGS,
    GO_NECKLACES,
    GO_SCAPULARS,
    GO_BRACELETS,
    GO_EARRINGS
}

/**
 * Subscriber subscribes to a topic (Publish/Subscriber Pattern) and not to the source of the topic (Observer Pattern).
 *
 * The MenuEventBus has a narrow use case:  emit menu navigation events to the app's BaseView instance.
 */
class MenuEventBus {

    // MutableSharedFlow Param replay: #  of values replayed to new subscribers (cannot be negative, defaults to zero).
    private val _events = MutableSharedFlow<MenuEvent>(replay = 0)

    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event: MenuEvent) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}
