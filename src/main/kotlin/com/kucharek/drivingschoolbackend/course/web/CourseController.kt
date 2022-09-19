package com.kucharek.drivingschoolbackend.course.web

import com.kucharek.drivingschoolbackend.course.CourseCreationResult
import com.kucharek.drivingschoolbackend.course.CourseCreationResultDto
import com.kucharek.drivingschoolbackend.course.CourseService
import com.kucharek.drivingschoolbackend.course.CreateNewCourseDto
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
    fun createCourse(
        @RequestBody createNewCourse: CreateNewCourseDto
    ): ResponseEntity<CourseCreationResultDto>
    {
        val creationResult = courseService.createCourse(createNewCourse)
        return if (creationResult.result == CourseCreationResult.CREATED) {
            ResponseEntity.ok(creationResult)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(creationResult)
        }
    }
}
