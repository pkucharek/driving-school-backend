package com.kucharek.drivingschoolbackend.course

import org.apache.commons.lang3.RandomStringUtils

internal fun courseCommand(
    email: String = "${RandomStringUtils.random(10, true, false)}.@test.com",
    nationalIdNumber: String = RandomStringUtils.random(11, false, true),
) = CreateNewCourseDto(
    firstName = RandomStringUtils.random(6, true, false),
    lastName = RandomStringUtils.random(6, true, false),
    nationalIdNumber = nationalIdNumber,
    email = email,
    courseCategory = CourseCategory.A
)
