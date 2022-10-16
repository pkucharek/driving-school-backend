package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.event.DomainCommand
import java.time.Instant

sealed class AccountCommand : DomainCommand

data class CreateAccount(
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
): AccountCommand()

data class ActivateAccount(
    val activationTimestamp: Instant,
    val isActive: Boolean = true
): AccountCommand()
