package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.account.AccountEvent
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
        eventStore: EventStore<CourseId, CourseEvent> = courseEventStore(),
        courseQueryRepository: CourseQueryRepository = courseQueryRepository(),
    ) = CourseService(
        accountService,
        eventStore,
        courseQueryRepository
    )

    @Bean
    fun courseEventStore() = EventStore<CourseId, CourseEvent>()

    @Bean
    fun courseQueryRepository() = CourseQueryInMemoryRepository()
}
