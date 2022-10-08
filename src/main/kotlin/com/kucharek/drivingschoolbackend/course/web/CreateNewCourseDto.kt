package com.kucharek.drivingschoolbackend.course.web

import com.kucharek.drivingschoolbackend.course.CourseCategory

data class CreateNewCourseDto(
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
    val courseCategory: CourseCategory,
)
