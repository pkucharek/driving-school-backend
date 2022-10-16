package com.kucharek.drivingschoolbackend.account.readmodel

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.AggregateDoesNotExist
import com.kucharek.drivingschoolbackend.event.DomainCommandError

class AccountQueryInMemoryRepository : AccountQueryRepository {
    private var records: Map<AccountId, AccountReadModel> = mapOf()

    override fun findByNationalIDNumber(nationalIdNumber: String) =
        findByPredicate { it.nationalIdNumber == nationalIdNumber }

    override fun findByEmail(email: String) =
        findByPredicate { it.email == email }

    override fun createReadModel(
        id: AccountId,
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
        isActive: Boolean
    ) {
        records = records +
            Pair(id, AccountReadModel(id, firstName, lastName, nationalIdNumber, email, isActive))
    }

    override fun findByPredicate(predicate: (AccountReadModel) -> Boolean):
        Either<DomainCommandError, AccountReadModel>
    {
        val foundRecord = records.filterValues(predicate)
        if (foundRecord.isEmpty()) return Either.Left(AggregateDoesNotExist)

        return Either.Right(foundRecord.iterator().next().value)
    }

    override fun accountActivated(id: AccountId) {
        records = records + Pair(id, records[id]!!.copy(isActive = true))
    }
}
