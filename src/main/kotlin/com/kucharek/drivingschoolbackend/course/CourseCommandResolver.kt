package com.kucharek.drivingschoolbackend.course

import arrow.core.Either
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import com.kucharek.drivingschoolbackend.event.EventStore
import java.util.*

class CourseCommandResolver(
    private val eventStore: EventStore<Course, CourseEvent>,
) {
    fun createCourse(
        id: UUID,
        courseCategory: CourseCategory
    ): CourseCreationResultDto {
        val course = Course()
        val events: Either<DomainCommandError, CourseEvent> = course.handle(CreateCourse(id, courseCategory))
        var courseId: UUID
        events.map { event ->
            eventStore.saveEvents(listOf(event))
            course.apply(event).let {
                courseId = event.eventMetaData.aggregateID
            }
            CourseCreationResultDto.created(courseId)
        }
        return CourseCreationResultDto.created(courseId)
    }
}
