package com.kucharek.drivingschoolbackend.account.activation

import com.kucharek.drivingschoolbackend.event.Aggregate
import com.kucharek.drivingschoolbackend.event.DomainEvent
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.time.Instant
import java.util.UUID

class ActivationLink private constructor() : Aggregate<ActivationLinkEvent> {
    var domainEvents: List<ActivationLinkEvent> = listOf()
    override lateinit var id: UUID
    private lateinit var userId: UUID
    private lateinit var expirationDate: Instant
    private lateinit var activationKey: ActivationKey
    private var isConsumed = false

    companion object {
        fun create(
            userId: UUID,
            expirationDate: Instant,
            activationKey: ActivationKey
        ): ActivationLink {
            val activationLinkId = UUID.randomUUID()
            return buildFrom(listOf(ActivationLinkCreated(
                EventMetaData(activationLinkId, UUID.randomUUID(), Instant.now()),
                userId, expirationDate, activationKey, isConsumed = false
            )))
        }

        fun buildFrom(events: List<ActivationLinkEvent>): ActivationLink {
            return events.fold(ActivationLink()) { account: ActivationLink, event: ActivationLinkEvent ->
                when (event) {
                    is ActivationLinkCreated -> account.applyActivationLinkCreated(event)
                }
            }
        }
    }

    private fun applyActivationLinkCreated(event: ActivationLinkCreated): ActivationLink {
        id = event.eventMetaData.aggregateID
        userId = event.userId
        expirationDate = event.expirationDate
        activationKey = event.activationKey
        isConsumed = event.isConsumed
        return this
    }
}

data class ActivationKey(
    val value: String,
)

sealed class ActivationLinkEvent : DomainEvent, java.io.Serializable

data class ActivationLinkCreated(
    override val eventMetaData: EventMetaData,
    val userId: UUID,
    val expirationDate: Instant,
    val activationKey: ActivationKey,
    val isConsumed: Boolean
) : ActivationLinkEvent()
