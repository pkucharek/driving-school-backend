package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainCommand
import java.io.Serializable
import java.util.*

sealed class CourseCommand : DomainCommand, Serializable

data class CreateCourse(
    val accountId: AccountId,
    val courseCategory: CourseCategory
): CourseCommand()
