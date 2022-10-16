package com.kucharek.drivingschoolbackend.account.activation.readmodel

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkId
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import java.time.Instant
import java.util.*

interface ActivationLinkQueryRepository {
    fun findByPredicate(predicate: (ActivationLinkReadModel) -> Boolean)
        : Either<DomainCommandError, ActivationLinkReadModel>
    fun createReadModel(
        id: ActivationLinkId,
        expirationDate: Instant,
        activationKey: ActivationKey,
        accountId: AccountId,
        isConsumed: Boolean
    )

    fun activationLinkConsumed(id: ActivationLinkId)
}
