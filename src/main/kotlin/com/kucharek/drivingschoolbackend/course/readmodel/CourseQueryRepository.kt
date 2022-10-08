package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.course.CourseCategory
import com.kucharek.drivingschoolbackend.course.CourseId
import java.util.*

interface CourseQueryRepository {
    fun createReadModel(
        id: CourseId,
        accountId: AccountId,
        courseCategory: CourseCategory,
    )
}
