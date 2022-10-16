package com.kucharek.drivingschoolbackend.account.activation

import arrow.core.Either
import arrow.core.flatMap
import com.kucharek.drivingschoolbackend.account.Account
import com.kucharek.drivingschoolbackend.account.AccountId
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkQueryRepository
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkReadModel
import com.kucharek.drivingschoolbackend.account.adapter.MockBaseUrlResolver
import com.kucharek.drivingschoolbackend.account.port.BaseUrlResolver
import com.kucharek.drivingschoolbackend.event.DomainCommandError
import com.kucharek.drivingschoolbackend.event.EventStore
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant
import java.time.Period
import java.util.*

class ActivationLinkService(
    private val eventStore: EventStore<ActivationLinkId, ActivationLinkEvent>,
    private val activationLinkQueryRepository: ActivationLinkQueryRepository,
    private val baseUrlResolver: BaseUrlResolver = MockBaseUrlResolver(),
) {

    fun generate(accountID: AccountId): String {
        val activationKey = RandomStringUtils.random(16, true, true)
        ActivationLink().apply {
            handle(CreateActivationLink(
                accountId = accountID,
                expirationDate = Instant.now().plus(Period.ofDays(1)),
                activationKey = ActivationKey(activationKey)
            )).map { event ->
                eventStore.saveEvent(event)

                // TODO replace with asynchronous read model update
                activationLinkQueryRepository.createReadModel(
                    id = event.metaData.aggregateID,
                    expirationDate = Instant.now().plus(Period.ofDays(1)),
                    activationKey = ActivationKey(activationKey),
                    accountId = accountID,
                    isConsumed = false
                )
            }
        }

        return "${baseUrlResolver.resolve()}/account/activate/$activationKey"
    }

    fun getBy(predicate: (ActivationLinkReadModel) -> Boolean) =
        activationLinkQueryRepository.findByPredicate(predicate)

    private fun getAggregate(id: ActivationLinkId)
        = eventStore.loadEvents(id).map { list ->
            list.fold(ActivationLink()) { acc, activationLinkEvent ->
                acc.applyEvent(activationLinkEvent)
            }
        }

    fun consumeLink(id: ActivationLinkId): Either<DomainCommandError, ActivationLinkId> =
        getAggregate(id).flatMap { activationLink ->
            activationLink.handle(ConsumeActivationLink()).map { event ->
                eventStore.saveEvent(event)
                activationLinkQueryRepository.activationLinkConsumed(activationLink.id)
                return Either.Right(event.metaData.aggregateID)
            }
        }
}
