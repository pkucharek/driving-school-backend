package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.event.DomainCommandError

sealed class CourseError(
    override val message: String
) : DomainCommandError(message)
