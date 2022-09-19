package com.kucharek.drivingschoolbackend.account.readmodel

import java.util.*

class AccountReadModel(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
    val isActive: Boolean,
)
