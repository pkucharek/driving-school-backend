package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.account.AccountCreationResult
import com.kucharek.drivingschoolbackend.account.AccountService
import com.kucharek.drivingschoolbackend.course.readmodel.CourseQueryRepository

class CourseService(
    private val accountService: AccountService,
    private val courseCommandResolver: CourseCommandResolver,
    private val courseQueryRepository: CourseQueryRepository,
) {
    fun createCourse(createNewCourse: CreateNewCourseDto)
        : CourseCreationResultDto
    {
        val createAccountResult = accountService.createAccount(
            createNewCourse.firstName,
            createNewCourse.lastName,
            createNewCourse.nationalIdNumber,
            createNewCourse.email
        )
        if (createAccountResult.result == AccountCreationResult.NOT_CREATED) {
            return CourseCreationResultDto.notCreated(
                createAccountResult.message
            )
        }
        return courseCommandResolver.createCourse(
            createAccountResult.accountId!!,
            createNewCourse.courseCategory
        ).let {
            courseQueryRepository.createReadModel(
                createAccountResult.accountId,
                it.id!!,
                createNewCourse.courseCategory
            )
            it
        }
    }
}
