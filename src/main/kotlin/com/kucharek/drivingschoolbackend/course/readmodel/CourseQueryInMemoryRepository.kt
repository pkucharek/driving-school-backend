package com.kucharek.drivingschoolbackend.course.readmodel

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.course.CourseCategory
import com.kucharek.drivingschoolbackend.course.CourseId
import java.util.*

class CourseQueryInMemoryRepository : CourseQueryRepository {
    private var records: Map<CourseId, CourseReadModel> = mapOf()

    override fun createReadModel(
        id: CourseId,
        accountId: AccountId,
        courseCategory: CourseCategory
    ) {
         records = records +
            Pair(id, CourseReadModel(id, accountId, courseCategory))
    }
}
