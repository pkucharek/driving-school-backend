package com.kucharek.drivingschoolbackend.account.activation

import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.DomainCommand
import java.time.Instant

sealed class ActivationLinkCommand : DomainCommand

data class CreateActivationLink(
    val accountId: AccountId,
    val expirationDate: Instant,
    val activationKey: ActivationKey,
) : ActivationLinkCommand()

data class ConsumeActivationLink(
    val isConsumed: Boolean = true
) : ActivationLinkCommand()
