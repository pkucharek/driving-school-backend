package com.kucharek.drivingschoolbackend.account.readmodel

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountAlreadyExists
import com.kucharek.drivingschoolbackend.account.AccountDoesNotExist
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainCommandError

interface AccountQueryRepository {
    fun findByPredicate(predicate: (AccountReadModel) -> Boolean): Either<DomainCommandError, AccountReadModel>
    fun notExistByNationalIDNumber(nationalIdNumber: String): Either<AccountAlreadyExists, AccountDoesNotExist>
    fun notExistByEmail(email: String): Either<AccountAlreadyExists, AccountDoesNotExist>
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
