package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.course.CourseCategory
import java.util.*

data class CourseReadModel(
    val id: UUID,
    val accountId: UUID,
    val courseCategory: CourseCategory,
)
