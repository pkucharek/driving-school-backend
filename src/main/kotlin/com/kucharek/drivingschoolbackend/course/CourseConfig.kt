package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.account.AccountService
import com.kucharek.drivingschoolbackend.course.readmodel.CourseQueryInMemoryRepository
import com.kucharek.drivingschoolbackend.course.readmodel.CourseQueryRepository
import com.kucharek.drivingschoolbackend.event.EventStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CourseConfig {

    @Bean
    fun courseService(
        accountService: AccountService,
        courseCommandResolver: CourseCommandResolver = courseCommandResolver(courseEventStore()),
        courseQueryRepository: CourseQueryRepository = courseQueryRepository(),
    ) = CourseService(
        accountService,
        courseCommandResolver,
        courseQueryRepository
    )

    @Bean
    fun courseCommandResolver(eventStore: EventStore<Course, CourseEvent>)
        = CourseCommandResolver(eventStore)

    @Bean
    fun courseEventStore() = EventStore<Course, CourseEvent>()

    @Bean
    fun courseQueryRepository() = CourseQueryInMemoryRepository()
}
