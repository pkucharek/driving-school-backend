package com.kucharek.drivingschoolbackend.account

import arrow.core.Either
import com.kucharek.drivingschoolbackend.event.Aggregate
import com.kucharek.drivingschoolbackend.event.EventMetaData
import java.util.*
import kotlin.properties.Delegates

data class AccountId(val uuid: UUID)

class Account : Aggregate<AccountId, AccountCommand, AccountError, AccountEvent>() {
    lateinit var id: AccountId
        private set
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var nationalIdNumber: String
    private lateinit var email: String
    private var isActive by Delegates.notNull<Boolean>()

    override fun applyEvent(event: AccountEvent): Account {
        return when (event) {
            is AccountCreated -> applyAccountCreated(event)
            is AccountActivated -> applyAccountActivated(event)
        }
    }

    private fun applyAccountCreated(event: AccountCreated): Account {
        id = event.metaData.aggregateID
        firstName = event.firstName
        lastName = event.lastName
        nationalIdNumber = event.nationalIdNumber
        email = event.email
        isActive = false
        return this
    }

    private fun applyAccountActivated(event: AccountActivated): Account {
        isActive = true
        return this
    }

    override fun handle(command: AccountCommand): Either<AccountError, AccountEvent> {
        return when (command) {
            is CreateAccount -> handleCreateCourse(command)
            is ActivateAccount -> handleActivateAccount(command)
        }
    }

    private fun handleCreateCourse(command: CreateAccount): Either<AccountError, AccountEvent> {
        return Either.Right(AccountCreated(
            metaData = EventMetaData(aggregateID = AccountId(UUID.randomUUID())),
            firstName = command.firstName,
            lastName = command.lastName,
            nationalIdNumber = command.nationalIdNumber,
            email = command.email
        ))
    }

    private fun handleActivateAccount(command: ActivateAccount): Either<AccountError, AccountEvent> {
        if (isActive) {
            return Either.Left(AccountAlreadyActivated)
        }
        return Either.Right(AccountActivated(
            metaData = EventMetaData(aggregateID = id),
            command.activationTimestamp
        ))
    }
}
