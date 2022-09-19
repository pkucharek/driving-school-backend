package com.kucharek.drivingschoolbackend.account.activation

import arrow.core.Option
import java.time.Instant
import java.util.*

interface ActivationLinkQueryRepository {
    fun findByPredicate(predicate: (ActivationLinkReadModel) -> Boolean)
        : Option<ActivationLinkReadModel>
    fun createReadModel(
        id: UUID,
        expirationDate: Instant,
        activationKey: ActivationKey,
        accountId: UUID,
        isConsumed: Boolean
    )
}
