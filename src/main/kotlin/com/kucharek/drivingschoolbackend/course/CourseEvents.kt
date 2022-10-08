package com.kucharek.drivingschoolbackend.course

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainEvent
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.io.Serializable

sealed class CourseEvent : DomainEvent<CourseId>, Serializable

data class CourseCreated(
    override val metaData: EventMetaData<CourseId>,
    val accountId: AccountId,
    val courseCategory: CourseCategory
) : CourseEvent()
