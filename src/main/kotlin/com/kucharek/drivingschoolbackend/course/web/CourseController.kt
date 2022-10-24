package com.kucharek.drivingschoolbackend.course.web

import com.kucharek.drivingschoolbackend.course.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
) {
    @PostMapping
    suspend fun createCourse(
        @RequestBody createNewCourse: CreateNewCourseDto
    ): ResponseEntity<Any>
    {
        return courseService.createCourse(createNewCourse).fold(
            { error -> ResponseEntity(error.message, HttpStatus.BAD_REQUEST) },
            { uuid -> ResponseEntity.ok(uuid) }
        )
    }
}
