package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.BaseTestSystem
import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class ActivationLinkTests: BaseTestSystem() {

    @Test
    fun `creates not consumed activation link after account is created`() {
        //given
        val createCourseCommand = courseCommand()

        //when
        system.courseService.createCourse(createCourseCommand)

        //then
        val account = system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        )
        assertThat(account.isDefined()).isTrue

        val activationLink = system.accountService.getActivationLinkByAccountId(
            account.orNull()!!.id
        )
        assertAll(
            { assertThat(activationLink.isDefined()).isTrue },
            { assertThat(activationLink.orNull()!!.isConsumed).isFalse },
        )
    }

    @Test
    fun `account is activated and activation link is consumed after activation link is used`() {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        val account = system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        ).orNull()!!
        val activationLink = system.accountService.getActivationLinkByAccountId(
            account.id
        ).orNull()!!

        //when
        val actionResult = system.accountService.useActivationLink(
            activationLink.activationKey
        )

        //then
        assertAll(
            { assertThat(actionResult.result).isEqualTo(ActivationResult.ACCOUNT_ACTIVATED) },
            {
                val resultAccount = system.accountService.getAccountByNationalIdNumber(
                    createCourseCommand.nationalIdNumber
                ).orNull()!!
                assertThat(resultAccount.isActive).isTrue
            },
            {
                val resultActivationLink = system.accountService.getActivationLinkByAccountId(
                    account.id
                ).orNull()!!
                assertThat(resultActivationLink.isConsumed).isTrue
            }
        )
    }

    @Test
    fun `consumed activation link cannot be consumed again`() {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        val account = system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        ).orNull()!!
        val activationLink = system.accountService.getActivationLinkByAccountId(
            account.id
        ).orNull()!!

        //when
        val actionResult = system.accountService.useActivationLink(activationLink.activationKey)

        //then
        assertThat(actionResult.result).isEqualTo(ActivationResult.LINK_ALREADY_USED)
    }

    @Test
    fun `not existing activationKey cannot be used`() {
        //given
        val nonExistingActivationKey = ActivationKey("123")

        //when
        val actionResult = system.accountService.useActivationLink(nonExistingActivationKey)

        //then
        assertThat(actionResult.result).isEqualTo(ActivationResult.NOT_FOUND)
    }
}
