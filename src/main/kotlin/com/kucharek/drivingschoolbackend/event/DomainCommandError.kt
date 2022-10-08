package com.kucharek.drivingschoolbackend.event

abstract class DomainCommandError(
    open val message: String
)

object AggregateDoesNotExist : DomainCommandError("Aggregate does not exist")
