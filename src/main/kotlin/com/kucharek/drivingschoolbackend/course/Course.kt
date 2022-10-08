package com.kucharek.drivingschoolbackend.course

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.*
import java.io.Serializable
import java.util.*

data class CourseId(val uuid: UUID)

class Course: Aggregate<CourseId, CourseCommand, CourseError, CourseEvent>() {
    lateinit var id: CourseId
        private set
    private lateinit var accountId: AccountId
    private lateinit var courseCategory: CourseCategory

    override fun applyEvent(event: CourseEvent): Course {
        return when (event) {
            is CourseCreated -> applyCourseCreated(event)
        }
    }

    private fun applyCourseCreated(event: CourseCreated): Course {
        id = event.metaData.aggregateID
        accountId = event.accountId
        courseCategory = event.courseCategory
        return this
    }

    override fun handle(command: CourseCommand): Either<CourseError, CourseEvent> {
        return when (command) {
            is CreateCourse -> handleCreateCourse(command)
        }
    }

    private fun handleCreateCourse(command: CreateCourse): Either<CourseError, CourseCreated> {
        return Either.Right(CourseCreated(
            EventMetaData(aggregateID = CourseId(UUID.randomUUID())),
            command.accountId,
            command.courseCategory
        ))
    }
}
