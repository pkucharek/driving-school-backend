package com.kucharek.drivingschoolbackend.account.activation.readmodel

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkDoesNotExist
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkId
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import java.time.Instant
import java.util.UUID

class ActivationLinkQueryInMemoryRepository : ActivationLinkQueryRepository {
    private var records: Map<ActivationLinkId, ActivationLinkReadModel> = mapOf()

    override fun createReadModel(
        id: ActivationLinkId,
        expirationDate: Instant,
        activationKey: ActivationKey,
        accountId: AccountId,
        isConsumed: Boolean
    ) {
        records = records +
            Pair(
                id,
                ActivationLinkReadModel(
                    id, expirationDate, activationKey, accountId, isConsumed
                )
            )
    }

    override fun findByPredicate(predicate: (ActivationLinkReadModel) -> Boolean):
        Either<DomainCommandError, ActivationLinkReadModel>
    {
        val foundRecord = records.filterValues(predicate)
        if (foundRecord.isEmpty()) return Either.Left(ActivationLinkDoesNotExist)

        return Either.Right(foundRecord.iterator().next().value)
    }
}
