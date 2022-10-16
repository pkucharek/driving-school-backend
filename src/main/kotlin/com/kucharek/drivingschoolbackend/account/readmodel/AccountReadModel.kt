package com.kucharek.drivingschoolbackend.account.readmodel

import com.kucharek.drivingschoolbackend.account.AccountId

data class AccountReadModel(
    val id: AccountId,
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
    val isActive: Boolean,
)
