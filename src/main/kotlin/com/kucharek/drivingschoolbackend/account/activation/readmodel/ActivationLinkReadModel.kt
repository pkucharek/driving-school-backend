package com.kucharek.drivingschoolbackend.account.activation.readmodel

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkId
import java.time.Instant
import java.util.*

data class ActivationLinkReadModel(
    val id: ActivationLinkId,
    val expirationDate: Instant,
    val activationKey: ActivationKey,
    val accountId: AccountId,
    val isConsumed: Boolean,
)
