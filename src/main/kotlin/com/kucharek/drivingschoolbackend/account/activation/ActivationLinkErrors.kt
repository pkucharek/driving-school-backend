package com.kucharek.drivingschoolbackend.account.activation

import com.kucharek.drivingschoolbackend.event.DomainCommandError

sealed class ActivationLinkError(
    override val message: String
) : DomainCommandError(message)

object ActivationLinkDoesNotExist: ActivationLinkError("Activation link does not exist")
object ActivationLinkAlreadyConsumed: ActivationLinkError("Activation link is already consumed")
