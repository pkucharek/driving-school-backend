package com.kucharek.drivingschoolbackend.event

import arrow.core.Either
import com.kucharek.drivingschoolbackend.course.CourseEvent

abstract class Aggregate<
    AggregateId,
    Command: DomainCommand,
    CommandError: DomainCommandError,
    Event : DomainEvent<AggregateId>
> {
    var domainEvents: List<CourseEvent> = listOf()

    abstract fun handle(command: Command): Either<CommandError, Event>
    abstract fun applyEvent(event: Event): Aggregate<AggregateId, Command, CommandError, Event>

    fun buildFrom(events: List<Event>): Aggregate<AggregateId, Command, CommandError, Event> {
        return events.fold(this) {
            aggregate: Aggregate<AggregateId, Command, CommandError, Event>,
            event: Event
        ->
            aggregate.applyEvent(event)
        }
    }
}
