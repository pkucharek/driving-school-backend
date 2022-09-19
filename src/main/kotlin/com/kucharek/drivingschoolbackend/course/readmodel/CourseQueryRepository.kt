package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.course.CourseCategory
import java.util.*

interface CourseQueryRepository {
    fun createReadModel(
        id: UUID,
        accountId: UUID,
        courseCategory: CourseCategory,
    )
}
