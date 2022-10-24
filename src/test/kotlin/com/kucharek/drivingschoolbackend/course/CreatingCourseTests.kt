package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.BaseTestSystem
import com.kucharek.drivingschoolbackend.account.AccountAlreadyExists
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreatingCourseTests: BaseTestSystem() {

    @Test
    fun `creates course`() = runTest {
        //given
        val createCourseCommand = courseCommand()

        //when
        val courseCreationResult = system.courseService.createCourse(createCourseCommand)

        //then
        courseCreationResult.map { uuid ->
            assertThat(uuid.toString()).isNotBlank()
        }
    }

    @Test
    fun `creates account after creating course`() = runTest {
        //given
        val createCourseCommand = courseCommand()

        //when
        system.courseService.createCourse(createCourseCommand)

        //then
        val account = system.accountService.getAccountBy {
            it.nationalIdNumber == createCourseCommand.nationalIdNumber
        }
        assertThat(account.isRight()).isTrue
    }

    @Test
    fun `does not create course when account with nationalIdNumber already exists`() = runTest {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        //when
        val createCourseResult = system.courseService.createCourse(createCourseCommand)

        //then
        createCourseResult.fold(
            { error -> assertThat(error).isInstanceOf(AccountAlreadyExists::class.java) },
            { throw Exception() }
        )
    }

    @Test
    fun `does not create course when account with email already exists`() = runTest {
        //given
        val previousCreateCourseCommand = courseCommand()
        system.courseService.createCourse(previousCreateCourseCommand)

        //when
        val createCourseResult = system.courseService.createCourse(
            courseCommand(email = previousCreateCourseCommand.email)
        )

        //then
        createCourseResult.fold(
            { error -> assertThat(error).isInstanceOf(AccountAlreadyExists::class.java) },
            {}
        )
    }
}
