package com.kucharek.drivingschoolbackend.account

import arrow.core.Either
import arrow.core.continuations.either
import com.kucharek.drivingschoolbackend.account.activation.*
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkReadModel
import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import com.kucharek.drivingschoolbackend.account.readmodel.AccountQueryRepository
import com.kucharek.drivingschoolbackend.account.readmodel.AccountReadModel
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import com.kucharek.drivingschoolbackend.event.EventStore
import java.time.Instant

class AccountService(
    private val eventStore: EventStore<AccountId, AccountEvent>,
    private val accountQueryRepository: AccountQueryRepository,
    private val emailSenderPort: EmailSenderPort,
    private val activationLinkService: ActivationLinkService,
) {
    suspend fun createAccount(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String,
    ): Either<DomainCommandError, Account> =
        either {
            accountQueryRepository.notExistByNationalIDNumber(nationalIdNumber).bind()
            accountQueryRepository.notExistByEmail(email).bind()

            val newAccount = Account()
            val event = newAccount.handle(CreateAccount(
                firstName = firstName,
                lastName = lastName,
                nationalIdNumber = nationalIdNumber,
                email = email
            )).bind()
            newAccount.applyEvent(event)
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
            newAccount
        }

    fun getAccountByNationalIdNumber(nationalIdNumber: String) =
        getAccountBy { it.nationalIdNumber == nationalIdNumber }

    fun getAccountBy(predicate: (AccountReadModel) -> Boolean) =
        accountQueryRepository.findByPredicate(predicate)

    fun getActivationLinkByAccountId(accountId: AccountId) =
        getActivationLinkBy { it.accountId == accountId }

    fun getActivationLinkBy(predicate: (ActivationLinkReadModel) -> Boolean) =
        activationLinkService.getBy(predicate)

    private fun getAggregate(id: AccountId) =
        eventStore.loadEvents(id).map { list ->
            list.fold(Account()) { acc, accountEvent ->
                acc.applyEvent(accountEvent)
            }
        }

    suspend fun useActivationLink(activationKey: ActivationKey)
        : Either<DomainCommandError, AccountId> =
        either {
            val activationLink = getActivationLinkBy { it.activationKey == activationKey }.bind()
            activationLinkService.consumeLink(activationLink.id).bind()
            val account = getAggregate(activationLink.accountId).bind()
            val resultEvent = account.handle(ActivateAccount(Instant.now())).bind()
            eventStore.saveEvent(resultEvent)
            accountQueryRepository.accountActivated(account.id)
            account.id
        }
}
