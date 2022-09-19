package com.kucharek.drivingschoolbackend.account.readmodel

import arrow.core.Option
import java.util.*

interface AccountQueryRepository {
    fun findByPredicate(predicate: (AccountReadModel) -> Boolean): Option<AccountReadModel>
    fun findByNationalIDNumber(nationalIdNumber: String): Option<AccountReadModel>
    fun findByEmail(email: String): Option<AccountReadModel>
    fun createReadModel(
        id: UUID,
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
        isActive: Boolean,
    )
}
