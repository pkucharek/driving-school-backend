package com.kucharek.drivingschoolbackend.account.activation

import arrow.core.Either
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.event.Aggregate
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.time.Instant
import java.util.*
import kotlin.properties.Delegates


data class ActivationKey(
    val value: String,
)

data class ActivationLinkId(val uuid: UUID)

class ActivationLink : Aggregate<ActivationLinkId, ActivationLinkCommand, ActivationLinkError, ActivationLinkEvent>() {
    lateinit var id: ActivationLinkId
        private set
    private lateinit var userId: AccountId
    private lateinit var expirationDate: Instant
    private lateinit var activationKey: ActivationKey
    private var isConsumed by Delegates.notNull<Boolean>()

    override fun applyEvent(event: ActivationLinkEvent): ActivationLink {
        return when (event) {
            is ActivationLinkCreated -> applyActivationLinkCreated(event)
        }
    }

    private fun applyActivationLinkCreated(event: ActivationLinkCreated): ActivationLink {
        id = event.metaData.aggregateID
        userId = event.userId
        expirationDate = event.expirationDate
        activationKey = event.activationKey
        isConsumed = event.isConsumed
        return this
    }

    override fun handle(command: ActivationLinkCommand): Either<ActivationLinkError, ActivationLinkEvent> {
        return when (command) {
            is CreateActivationLink -> handleCreateActivationLink(command)
        }
    }

    private fun handleCreateActivationLink(command: CreateActivationLink): Either<ActivationLinkError, ActivationLinkEvent> {
        return Either.Right(ActivationLinkCreated(
            metaData = EventMetaData(aggregateID = ActivationLinkId(UUID.randomUUID())),
            userId = command.accountId,
            expirationDate = command.expirationDate,
            activationKey = command.activationKey,
            isConsumed = false
        ))
    }
}
