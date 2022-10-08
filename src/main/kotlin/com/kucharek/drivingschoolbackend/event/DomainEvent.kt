package com.kucharek.drivingschoolbackend.event

interface DomainEvent<AggregateId> {
    val metaData: EventMetaData<AggregateId>
}
