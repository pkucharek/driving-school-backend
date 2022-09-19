package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.event.Aggregate
import com.kucharek.drivingschoolbackend.event.DomainCommand
import com.kucharek.drivingschoolbackend.event.DomainEvent
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.io.Serializable
import java.time.Instant
import java.util.UUID

class Account private constructor() : Aggregate<AccountEvent> {
    var domainEvents: List<AccountEvent> = listOf()
    override lateinit var id: UUID
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var nationalIdNumber: String
    private lateinit var email: String
    private var isActive = false

    companion object {
        fun create(
            firstName: String,
            lastName: String,
            nationalIdNumber: String,
            email: String
        ): Account {
            val accountId = UUID.randomUUID()
            return buildFrom(listOf(AccountCreated(
                EventMetaData(accountId, UUID.randomUUID(), Instant.now()),
                firstName, lastName, nationalIdNumber, email
            )))
        }

        fun buildFrom(events: List<AccountEvent>): Account {
            return events.fold(Account()) { account: Account, event: AccountEvent ->
                when (event) {
                    is AccountCreated -> account.applyAccountCreated(event)
                }
            }
        }
    }

    private fun applyAccountCreated(event: AccountCreated): Account {
        id = event.eventMetaData.aggregateID
        firstName = event.firstName
        lastName = event.lastName
        nationalIdNumber = event.nationalIdNumber
        email = event.email
        return this
    }
}

sealed class AccountEvent : DomainEvent, Serializable

data class AccountCreated(
    override val eventMetaData: EventMetaData,
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
) : AccountEvent()

sealed class AccountCommand : DomainCommand, Serializable

data class CreateAccount(
    val firstName: String,
    val lastName: String,
    val nationalIdNumber: String,
    val email: String,
): AccountCommand()
