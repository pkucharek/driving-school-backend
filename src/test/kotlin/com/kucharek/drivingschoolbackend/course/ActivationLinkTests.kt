package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.BaseTestSystem
import com.kucharek.drivingschoolbackend.account.AccountAlreadyActivated
import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkDoesNotExist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class ActivationLinkTests : BaseTestSystem() {

    @Test
    fun `creates not consumed activation link after account is created`() {
        //given
        val createCourseCommand = courseCommand()

        //when
        system.courseService.createCourse(createCourseCommand)

        //then
        system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        ).map { account ->
            system.accountService.getActivationLinkByAccountId(
                account.id
            ).map { link ->
                assertThat(link.isConsumed).isFalse
            }
        }.fold({ error -> throw AssertionError(error) }, {})
    }

    @Test
    fun `account is activated and activation link is consumed after activation link is used`() {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        ).map { account ->
            system.accountService.getActivationLinkByAccountId(
                account.id
            ).map { activationLink ->

                //when
                system.accountService.useActivationLink(
                    activationLink.activationKey

                    //then
                ).map { uuid ->
                    assertAll(
                        { assertThat(uuid).isEqualTo(account.id) },
                        {
                            system.accountService.getAccountByNationalIdNumber(
                                createCourseCommand.nationalIdNumber
                            ).map { modifiedAccount ->
                                println("Modified account = $modifiedAccount")
                                assertThat(modifiedAccount.isActive).isTrue
                            }
                        },
                        {
                            system.accountService.getActivationLinkByAccountId(
                                account.id
                            ).map { resultActivationLink ->
                                assertThat(resultActivationLink.isConsumed).isTrue
                            }
                        }
                    )
                }
            }
        }
    }

    @Test
    fun `consumed activation link cannot be consumed again`() {
        //given
        val createCourseCommand = courseCommand()
        system.courseService.createCourse(createCourseCommand)

        val accountByNationalIdNumber = system.accountService.getAccountByNationalIdNumber(
            createCourseCommand.nationalIdNumber
        )
        val account = accountByNationalIdNumber.orNull()!!
        val activationLink = system.accountService.getActivationLinkByAccountId(
            account.id
        ).orNull()!!

        //when
        val actionResult = system.accountService.useActivationLink(activationLink.activationKey)

        //then
        actionResult.mapLeft { error ->
            assertThat(error).isInstanceOf(AccountAlreadyActivated::class.java)
        }
    }

    @Test
    fun `not existing activationKey cannot be used`() {
        //given
        val nonExistingActivationKey = ActivationKey("123")

        //when
        val actionResult = system.accountService.useActivationLink(nonExistingActivationKey)

        //then
        actionResult.mapLeft { error ->
            assertThat(error).isEqualTo(ActivationLinkDoesNotExist)
        }
    }
}
