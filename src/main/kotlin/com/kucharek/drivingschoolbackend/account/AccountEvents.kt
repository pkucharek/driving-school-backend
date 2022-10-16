package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.event.DomainEvent
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.io.Serializable
import java.time.Instant

sealed class AccountEvent : DomainEvent<AccountId>, Serializable

data class AccountCreated(
    override val metaData: EventMetaData<AccountId>,
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
) : AccountEvent()

data class AccountActivated(
    override val metaData: EventMetaData<AccountId>,
    val activationTimestamp: Instant,
    val isActive: Boolean = true
): AccountEvent()
