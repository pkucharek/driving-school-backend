package com.kucharek.drivingschoolbackend

import com.kucharek.drivingschoolbackend.account.AccountService
import com.kucharek.drivingschoolbackend.course.CourseService
import org.springframework.context.annotation.Bean

class SystemConfiguration {
    @Bean
    fun system(accountService: AccountService, courseService: CourseService)
        = System(accountService, courseService)
}

class System(
    val accountService: AccountService,
    val courseService: CourseService,
)
