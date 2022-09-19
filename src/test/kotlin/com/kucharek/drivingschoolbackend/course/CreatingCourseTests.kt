package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.BaseTestSystem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

class CreatingCourseTests: BaseTestSystem() {

    @Test
    fun `should create course`() {
        //given
        val createCourseCommand = courseCommand()

        //when
        val courseCreationResult = system.courseService.createCourse(createCourseCommand)

        //then
        assertAll(
            { assertThat(courseCreationResult.result).isEqualTo(CourseCreationResult.CREATED) },
            { assertThat(courseCreationResult.id).isNotNull() },
            { assertThat(courseCreationResult.message).isEmpty() },
        )
    }

    @Test
    fun `should create account when creating course`() {
        //given
        val createCourseCommand = courseCommand()

        //when
        system.courseService.createCourse(createCourseCommand)

        //then
        val account = system.accountService.getAccountBy {
            it.nationalIdNumber == createCourseCommand.nationalIdNumber
        }
        assertThat(account.isDefined()).isTrue
    }

    @Test
    fun `does not create course when account with nationalIdNumber already exists`() {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        //when
        val createCourseResult = system.courseService.createCourse(createCourseCommand)

        //then
        assertAll(
            { assertThat(createCourseResult.result).isEqualTo(CourseCreationResult.NOT_CREATED) },
            { assertThat(createCourseResult.id).isNull() },
            { assertThat(createCourseResult.message).contains("national ID number") },
        )
    }

    @Test
    fun `does not create course when account with email already exists`() {
        //given
        val previousCreateCourseCommand = courseCommand()
        system.courseService.createCourse(previousCreateCourseCommand)

        //when
        val createCourseResult = system.courseService.createCourse(
            courseCommand(email = previousCreateCourseCommand.email)
        )

        //then
        assertAll(
            { assertThat(createCourseResult.result).isEqualTo(CourseCreationResult.NOT_CREATED) },
            { assertThat(createCourseResult.id).isNull() },
            { assertThat(createCourseResult.message).contains("e-mail") },
        )
    }
}
