package com.kucharek.drivingschoolbackend.course

import arrow.core.Either
import arrow.core.continuations.either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.account.AccountService
import com.kucharek.drivingschoolbackend.course.readmodel.CourseQueryRepository
import com.kucharek.drivingschoolbackend.course.web.CreateNewCourseDto
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import com.kucharek.drivingschoolbackend.event.EventStore
import java.util.*

class CourseService(
    private val accountService: AccountService,
    private val eventStore: EventStore<CourseId, CourseEvent>,
    private val courseQueryRepository: CourseQueryRepository,
) {
    suspend fun createCourse(createNewCourse: CreateNewCourseDto)
        : Either<DomainCommandError, CourseId> =
        either {
            val createdAccount = accountService.createAccount(
                firstName = createNewCourse.firstName,
                lastName = createNewCourse.lastName,
                nationalIdNumber = createNewCourse.nationalIdNumber,
                email = createNewCourse.email
            ).bind()
            val newCourse = Course()
            val event = newCourse
                .handle(CreateCourse(createdAccount.id, createNewCourse.courseCategory))
                .bind()

            newCourse.applyEvent(event)

            eventStore.saveEvent(event)

            // TODO replace with asynchronous read model update
            courseQueryRepository.createReadModel(
                id = newCourse.id,
                accountId = createdAccount.id,
                courseCategory = createNewCourse.courseCategory
            )

            newCourse.id
        }
}
