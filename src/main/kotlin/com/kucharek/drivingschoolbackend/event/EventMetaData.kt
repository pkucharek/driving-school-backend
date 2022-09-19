package com.kucharek.drivingschoolbackend.event

import java.time.Instant
import java.util.*

data class EventMetaData(
    val aggregateID: UUID = UUID.randomUUID(),
    val eventID: UUID = UUID.randomUUID(),
    val timestamp: Instant = Instant.now(),
)
