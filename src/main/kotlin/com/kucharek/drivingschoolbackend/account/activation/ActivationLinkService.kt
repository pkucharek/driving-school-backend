package com.kucharek.drivingschoolbackend.account.activation

import com.kucharek.drivingschoolbackend.event.EventStore
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant
import java.util.*

class ActivationLinkService(
    private val eventStore: EventStore<ActivationLink, ActivationLinkEvent>,
    private val activationLinkQueryRepository: ActivationLinkQueryRepository,
    private val baseUrlResolver: BaseUrlResolver = MockBaseUrlResolver(),
) {

    fun generate(accountID: UUID): String {
        val activationKey = RandomStringUtils.random(16, true, true)
        val activationLink = ActivationLink.create(
            accountID,
            Instant.now(),
            ActivationKey(activationKey)
        )
        eventStore.saveEvents(activationLink.domainEvents)
        activationLinkQueryRepository.createReadModel(
            activationLink.id,
            Instant.now(),
            ActivationKey(activationKey),
            accountID,
            isConsumed = false
        )
        return "${baseUrlResolver.resolve()}/account/activate/$activationKey"
    }

    fun getBy(predicate: (ActivationLinkReadModel) -> Boolean)
            = activationLinkQueryRepository.findByPredicate {
                predicate(it)
            }
}
