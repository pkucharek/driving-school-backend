package com.kucharek.drivingschoolbackend.event

import java.time.Instant
import java.util.*

data class EventMetaData<AggregateID>(
    val aggregateID: AggregateID,
    val eventID: UUID = UUID.randomUUID(),
    val timestamp: Instant = Instant.now(),
)
