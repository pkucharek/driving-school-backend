package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.course.CourseCategory
import com.kucharek.drivingschoolbackend.course.CourseId
import java.util.*

data class CourseReadModel(
    val id: CourseId,
    val accountId: AccountId,
    val courseCategory: CourseCategory,
)
