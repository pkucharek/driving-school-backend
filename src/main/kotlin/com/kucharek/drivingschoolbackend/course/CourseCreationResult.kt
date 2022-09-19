package com.kucharek.drivingschoolbackend.course

import java.util.*

data class CourseCreationResultDto(
    val result: CourseCreationResult,
    val id: UUID?,
    val message: String,
) {
    companion object {
        fun notCreated(message: String)
            = CourseCreationResultDto(
                CourseCreationResult.NOT_CREATED, null, message
            )

        fun created(courseId: UUID): CourseCreationResultDto
            = CourseCreationResultDto(
                CourseCreationResult.CREATED, courseId, ""
            )
    }
}

enum class CourseCreationResult {
    CREATED,
    NOT_CREATED,
}
