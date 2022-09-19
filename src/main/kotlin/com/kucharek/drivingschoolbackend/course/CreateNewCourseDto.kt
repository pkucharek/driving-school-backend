package com.kucharek.drivingschoolbackend.course

data class CreateNewCourseDto(
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
    val courseCategory: CourseCategory,
)
