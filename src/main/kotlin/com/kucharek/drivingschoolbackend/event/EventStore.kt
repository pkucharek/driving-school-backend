package com.kucharek.drivingschoolbackend.event

import arrow.core.Either
import java.util.*

class EventStore<AggregateID, E : DomainEvent<AggregateID>> {
    private var events: Map<AggregateID, List<E>> = mapOf()

    fun saveEvent(domainEvent: E) = saveEvents(listOf(domainEvent))

    fun saveEvents(domainEvents: List<E>) {
        domainEvents.forEach {
            val previousEvents: List<E> = events[it.metaData.aggregateID] ?: listOf()
            events = events + Pair(
                it.metaData.aggregateID,
                previousEvents + domainEvents
            )
        }
    }

    fun loadEvents(id: AggregateID): Either<DomainCommandError, List<E>> =
        if (events[id] != null) Either.Right(events[id]!!)
        else Either.Left(AggregateDoesNotExist)
}
