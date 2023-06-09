package io.dongxi.view


import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class MenuEvent {
    SHOW_HOME,
    SHOW_ANEIS,
    SHOW_COLARES,

    // etc...
    LOGOUT;
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
