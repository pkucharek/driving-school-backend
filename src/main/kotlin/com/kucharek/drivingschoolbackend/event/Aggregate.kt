package com.kucharek.drivingschoolbackend.event

import arrow.core.Either
import com.kucharek.drivingschoolbackend.course.CourseEvent
import java.util.*

abstract class Aggregate<
    Command: DomainCommand,
    CommandError: DomainCommandError,
    Event : DomainEvent
> {
    lateinit var id: UUID
    var domainEvents: List<CourseEvent> = listOf()

    abstract fun handle(command: Command): Either<CommandError, Event>
    abstract fun apply(event: Event): Aggregate<Command, CommandError, Event>

    fun buildFrom(events: List<Event>): Aggregate<Command, CommandError, Event> {
        return events.fold(this) {
            aggregate: Aggregate<Command, CommandError, Event>,
            event: Event
        ->
            aggregate.apply(event)
        }
    }
}
