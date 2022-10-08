package com.kucharek.drivingschoolbackend.account.readmodel

import arrow.core.Either
import arrow.core.Option
import com.kucharek.drivingschoolbackend.account.AccountError
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import java.util.*

interface AccountQueryRepository {
    fun findByPredicate(predicate: (AccountReadModel) -> Boolean): Either<DomainCommandError, AccountReadModel>
    fun findByNationalIDNumber(nationalIdNumber: String): Either<DomainCommandError, AccountReadModel>
    fun findByEmail(email: String): Either<DomainCommandError, AccountReadModel>
    fun createReadModel(
        id: AccountId,
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
        isActive: Boolean,
    )

    fun accountActivated(id: AccountId)
}
