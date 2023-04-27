package io.dongxi.page.panel.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


/**
 * Subscriber subscribes to a topic (Publish/Subscriber Pattern) and not to the source of the topic (Observer Pattern).
 *
 * The MenuEventBus has a narrow use case:  emit menu navigation events to the app's BaseView instance.
 */
class AccessorySelectEventBus {
    // MutableSharedFlow Param replay: #  of values replayed to new subscribers (cannot be negative, defaults to zero).
    private val _events = MutableSharedFlow<AccessorySelectEvent>(replay = 0)

    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event: AccessorySelectEvent) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}