package com.kucharek.drivingschoolbackend.account.activation

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import java.time.Instant
import java.util.UUID

class ActivationLinkQueryInMemoryRepository : ActivationLinkQueryRepository {
    private var records: Map<UUID, ActivationLinkReadModel> = mapOf()

    override fun createReadModel(
        id: UUID,
        expirationDate: Instant,
        activationKey: ActivationKey,
        accountId: UUID,
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
        Option<ActivationLinkReadModel>
    {
        val foundRecord = records.filterValues(predicate)
        if (foundRecord.isEmpty()) return None

        return Some(foundRecord.iterator().next().value)
    }
}
