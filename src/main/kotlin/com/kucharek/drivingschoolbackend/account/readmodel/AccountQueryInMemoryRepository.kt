package com.kucharek.drivingschoolbackend.account.readmodel

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import java.util.UUID

class AccountQueryInMemoryRepository : AccountQueryRepository {
    private var records: Map<UUID, AccountReadModel> = mapOf()

    override fun findByNationalIDNumber(nationalIdNumber: String): Option<AccountReadModel> {
        return findByPredicate { it.nationalIdNumber == nationalIdNumber }
    }

    override fun findByEmail(email: String): Option<AccountReadModel> {
        return findByPredicate { it.email == email }
    }

    override fun createReadModel(
        id: UUID,
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
        isActive: Boolean,
    ) {
        records = records +
            Pair(id, AccountReadModel(id, firstName, lastName, nationalIdNumber, email, isActive))
    }

    override fun findByPredicate(predicate: (AccountReadModel) -> Boolean):
        Option<AccountReadModel>
    {
        val foundRecord = records.filterValues(predicate)
        if (foundRecord.isEmpty()) return None

        return Some(foundRecord.iterator().next().value)
    }
}
