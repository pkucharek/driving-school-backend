package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.course.CourseCategory
import java.util.*

class CourseQueryInMemoryRepository : CourseQueryRepository {
    private var records: Map<UUID, CourseReadModel> = mapOf()

    override fun createReadModel(
        id: UUID,
        accountId: UUID,
        courseCategory: CourseCategory
    ) {
         records = records +
            Pair(id, CourseReadModel(id, accountId, courseCategory))
    }
}
