package com.kucharek.drivingschoolbackend.course

import arrow.core.Either
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
    fun createCourse(createNewCourse: CreateNewCourseDto)
        : Either<DomainCommandError, CourseId>
    {
        //TODO replace with flatMap
        return accountService.createAccount(
            firstName = createNewCourse.firstName,
            lastName = createNewCourse.lastName,
            nationalIdNumber = createNewCourse.nationalIdNumber,
            email = createNewCourse.email
        ).fold(
            { error -> Either.Left(error) },
            { account ->
                Either.Right(Course().apply {
                    handle(CreateCourse(account.id, createNewCourse.courseCategory)).map { event ->
                        applyEvent(event)
                        eventStore.saveEvent(event)

                        // TODO replace with asynchronous read model update
                        courseQueryRepository.createReadModel(
                            id = id,
                            accountId = account.id,
                            courseCategory = createNewCourse.courseCategory
                        )
                        this
                    }
                }.id)
            }
        )
    }
}
