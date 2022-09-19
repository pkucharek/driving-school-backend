package com.kucharek.drivingschoolbackend.event

import arrow.core.Option
import java.util.*

class EventStore<
    T : Aggregate<out DomainCommand, out DomainCommandError, out DomainEvent>,
    E : DomainEvent
> {
    private var events: Map<UUID, List<E>> = mapOf()

    fun saveEvents(domainEvents: List<E>) {
        domainEvents.forEach {
            val previousEvents: List<E> = events[it.eventMetaData.aggregateID] ?: listOf()
            events = events + Pair(
                it.eventMetaData.aggregateID,
                previousEvents + domainEvents
            )
        }
    }

    fun buildAggregate(id: UUID): Option<T> {
        val list = events[id]

    }
}
