package com.kucharek.drivingschoolbackend

import com.kucharek.drivingschoolbackend.account.AccountConfig
import com.kucharek.drivingschoolbackend.course.CourseConfig

internal fun testSystemConfiguration(): System {
    val accountService = AccountConfig().accountService()
    val courseService = CourseConfig().courseService(
        accountService
    )

    return SystemConfiguration().system(accountService, courseService)
}

open class BaseTestSystem {
    protected val system: System = testSystemConfiguration()
}
