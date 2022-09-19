package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.account.activation.ActivationKey
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkReadModel
import com.kucharek.drivingschoolbackend.account.activation.ActivationLinkService
import com.kucharek.drivingschoolbackend.account.activation.ActivationResultDto
import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import com.kucharek.drivingschoolbackend.account.readmodel.AccountQueryRepository
import com.kucharek.drivingschoolbackend.account.readmodel.AccountReadModel
import com.kucharek.drivingschoolbackend.event.EventStore
import java.util.*

class AccountService(
    private val eventStore: EventStore<Account, AccountEvent>,
    private val accountQueryRepository: AccountQueryRepository,
    private val emailSenderPort: EmailSenderPort,
    private val activationLinkService: ActivationLinkService,
) {
    fun createAccount(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String
    ) : AccountCreationResultDto {
        val findByNationalIDNumber =
            accountQueryRepository.findByNationalIDNumber(nationalIdNumber)
        if (findByNationalIDNumber.isDefined())
            return AccountCreationResultDto.alreadyExistsByNationalIDNumber(
                nationalIdNumber
            )

        val findByEmail = accountQueryRepository.findByEmail(email)
        if (findByEmail.isDefined()) {
            return AccountCreationResultDto.alreadyExistsByEmail(
                email
            )
        }

        return createInternalAccount(
            firstName, lastName, nationalIdNumber, email
        ).let {
            accountQueryRepository.createReadModel(
                it.accountId!!, firstName, lastName, nationalIdNumber, email, isActive = false
            )
            emailSenderPort.sendActivationEmail(
                email,
                firstName,
                activationLinkService.generate(it.accountId)
            )
            it
        }
    }

    fun createInternalAccount(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        email: String
    ): AccountCreationResultDto {
        val createdAccount =
            Account.create(firstName, lastName, nationalIdNumber, email)
        eventStore.saveEvents(createdAccount.domainEvents)
        return AccountCreationResultDto.created(createdAccount.id)
    }

    fun getAccountByNationalIdNumber(nationalIdNumber: String)
        = getAccountBy { it.nationalIdNumber == nationalIdNumber }

    fun getAccountBy(predicate: (AccountReadModel) -> Boolean)
        = accountQueryRepository.findByPredicate {
            predicate(it)
        }

    fun getActivationLinkByAccountId(accountId: UUID)
        = getActivationLinkBy { it.accountId == accountId }

    fun getActivationLinkBy(predicate: (ActivationLinkReadModel) -> Boolean)
        = activationLinkService.getBy(predicate)

    fun useActivationLink(activationKey: ActivationKey): ActivationResultDto {
        val activationLink = getActivationLinkBy { it.activationKey == activationKey }
        if (activationLink.isEmpty()) return ActivationResultDto.notFound()

        activationLink.map {
            if (it.isConsumed) return ActivationResultDto.alreadyUsed()

            return getAccountBy { account -> account.id == it.accountId }.map { account ->
                eventStore.
            }
        }
    }
}
