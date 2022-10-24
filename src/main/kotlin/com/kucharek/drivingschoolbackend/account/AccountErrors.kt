package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.event.DomainCommandError

sealed class AccountError(
    override val message: String
) : DomainCommandError(message)

object AccountAlreadyExists : AccountError("Account already exists")
object AccountAlreadyActivated : AccountError("Account was already activated")

object AccountDoesNotExist
