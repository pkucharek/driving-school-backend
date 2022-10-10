package com.kucharek.drivingschoolbackend.account

import arrow.core.Either
import arrow.core.flatMap
import com.kucharek.drivingschoolbackend.account.activation.*
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkReadModel
import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import com.kucharek.drivingschoolbackend.account.readmodel.AccountQueryRepository
import com.kucharek.drivingschoolbackend.account.readmodel.AccountReadModel
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import com.kucharek.drivingschoolbackend.event.EventStore
import java.time.Instant
import java.util.*

class AccountService(
    private val eventStore: EventStore<AccountId, AccountEvent>,
    private val accountQueryRepository: AccountQueryRepository,
    private val emailSenderPort: EmailSenderPort,
    private val activationLinkService: ActivationLinkService,
) {
    fun createAccount(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
    ): Either<DomainCommandError, Account> =
        // TODO replace with map, flatMap
        accountQueryRepository.findByNationalIDNumber(nationalIdNumber).fold(
            {
                accountQueryRepository.findByEmail(email).fold(
                    {
                        Either.Right(Account().apply {
                            handle(CreateAccount(
                                firstName = firstName,
                                lastName = lastName,
                                nationalIdNumber = nationalIdNumber,
                                email = email
                            )).map { event ->
                                applyEvent(event)
                                eventStore.saveEvent(event)
                                val createdAccountId = event.metaData.aggregateID

                                // TODO replace with asynchronous read model update
                                accountQueryRepository.createReadModel(
                                    id = createdAccountId,
                                    firstName = firstName,
                                    lastName = lastName,
                                    nationalIdNumber = nationalIdNumber,
                                    email = email,
                                    isActive = false
                                )
                                emailSenderPort.sendActivationEmail(
                                    email,
                                    firstName,
                                    activationLinkService.generate(createdAccountId)
                                )
                                Either.Right(this)
                            }
                        })
                    },
                    { Either.Left(AccountAlreadyExists) }
                )
            },
            { Either.Left(AccountAlreadyExists) }
        )

    fun getAccountByNationalIdNumber(nationalIdNumber: String) =
        getAccountBy { it.nationalIdNumber == nationalIdNumber }

    fun getAccountBy(predicate: (AccountReadModel) -> Boolean) =
        accountQueryRepository.findByPredicate(predicate)

    fun getActivationLinkByAccountId(accountId: AccountId) =
        getActivationLinkBy { it.accountId == accountId }

    fun getActivationLinkBy(predicate: (ActivationLinkReadModel) -> Boolean) =
        activationLinkService.getBy(predicate)

    fun useActivationLink(activationKey: ActivationKey): Either<DomainCommandError, AccountId> =
        getActivationLinkBy { it.activationKey == activationKey }.flatMap { activationLink ->
            if (activationLink.isConsumed) {
                return Either.Left(ActivationLinkAlreadyConsumed)
            } else {
                eventStore.loadEvents(activationLink.accountId).map { list ->
                    list.fold(Account()) { acc, accountEvent ->
                        acc.applyEvent(accountEvent)
                    }
                }.flatMap { account ->
                    account.handle(ActivateAccount(Instant.now())).map { event ->
                        eventStore.saveEvent(event)
                        accountQueryRepository.accountActivated(event.metaData.aggregateID)
                        return Either.Right(event.metaData.aggregateID)
                    }
                }
            }
        }
}
