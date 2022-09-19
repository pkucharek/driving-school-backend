package com.kucharek.drivingschoolbackend.account.activation

import java.time.Instant
import java.util.*

data class ActivationLinkReadModel(
    val id: UUID,
    val expirationDate: Instant,
    val activationKey: ActivationKey,
    val accountId: UUID,
    val isConsumed: Boolean,
)
