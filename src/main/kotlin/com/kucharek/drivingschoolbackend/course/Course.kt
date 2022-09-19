package com.kucharek.drivingschoolbackend.course

import arrow.core.Either
import com.kucharek.drivingschoolbackend.event.*
import java.io.Serializable
import java.util.*

class Course: Aggregate<CourseCommand, DomainCommandError, CourseEvent>() {
    private lateinit var accountId: UUID
    private lateinit var courseCategory: CourseCategory

    override fun apply(event: CourseEvent): Course {
        return when (event) {
            is CourseCreated -> applyCourseCreated(event)
        };
    }

    private fun applyCourseCreated(event: CourseCreated): Course {
        id = event.eventMetaData.aggregateID
        accountId = event.accountId
        courseCategory = event.courseCategory
        return this
    }

    override fun handle(command: CourseCommand): Either<DomainCommandError, CourseEvent> {
        return when (command) {
            is CreateCourse -> handleCreateCourse(command)
        }
    }

    private fun handleCreateCourse(command: CreateCourse): Either<DomainCommandError, CourseCreated> {
        return Either.Right(CourseCreated(
            EventMetaData(),
            command.accountId,
            command.courseCategory
        ))
    }
}

sealed class CourseEvent : DomainEvent, Serializable

data class CourseCreated(
    override val eventMetaData: EventMetaData,
    val accountId: UUID,
    val courseCategory: CourseCategory
) : CourseEvent()

sealed class CourseCommand : DomainCommand, Serializable

data class CreateCourse(
    val accountId: UUID,
    val courseCategory: CourseCategory
): CourseCommand()
