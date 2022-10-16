package com.kucharek.drivingschoolbackend.account.activation

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainEvent
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.time.Instant
import java.util.*

sealed class ActivationLinkEvent : DomainEvent<ActivationLinkId>, java.io.Serializable

data class ActivationLinkCreated(
    override val metaData: EventMetaData<ActivationLinkId>,
    val userId: AccountId,
    val expirationDate: Instant,
    val activationKey: ActivationKey,
    val isConsumed: Boolean
) : ActivationLinkEvent()

data class ActivationLinkConsumed(
    override val metaData: EventMetaData<ActivationLinkId>,
    val isConsumed: Boolean = true
) : ActivationLinkEvent()
