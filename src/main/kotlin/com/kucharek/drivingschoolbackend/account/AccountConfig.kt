package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.account.activation.*
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkQueryInMemoryRepository
import com.kucharek.drivingschoolbackend.account.activation.readmodel.ActivationLinkQueryRepository
import com.kucharek.drivingschoolbackend.account.adapter.ConsoleEmailSender
import com.kucharek.drivingschoolbackend.account.adapter.JavaMailEmailSender
import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import com.kucharek.drivingschoolbackend.account.readmodel.AccountQueryInMemoryRepository
import com.kucharek.drivingschoolbackend.account.readmodel.AccountQueryRepository
import com.kucharek.drivingschoolbackend.event.EventStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class AccountConfig {
    @Bean
    fun accountService(
        activationLinkService: ActivationLinkService
            = activationLinkService(),
        eventStore: EventStore<AccountId, AccountEvent>
            = accountEventStore(),
        accountQueryRepository: AccountQueryRepository
            = accountQueryRepository(),
        emailSenderPort: EmailSenderPort
            = ConsoleEmailSender(),
    ) = AccountService(
        eventStore,
        accountQueryRepository,
        emailSenderPort,
        activationLinkService,
    )

    @Bean
    fun activationLinkService(
        eventStore: EventStore<ActivationLinkId, ActivationLinkEvent>
            = activationLinkEventStore(),
        activationLinkQueryRepository: ActivationLinkQueryRepository
            = activationLinkQueryRepository(),
    ) =
        ActivationLinkService(
            eventStore,
            activationLinkQueryRepository
        )

    @Suppress(
        "SpringJavaInjectionPointsAutowiringInspection",
        "JavaMailSender bean is correctly defined"
    )
    @Bean
    fun javaMailEmailSender(
        javaMailSender: JavaMailSender
    ) = JavaMailEmailSender(javaMailSender)

    @Bean
    fun accountEventStore() = EventStore<AccountId, AccountEvent>()

    @Bean
    fun activationLinkEventStore() = EventStore<ActivationLinkId, ActivationLinkEvent>()

    @Bean
    fun accountQueryRepository() = AccountQueryInMemoryRepository()

    @Bean
    fun activationLinkQueryRepository() = ActivationLinkQueryInMemoryRepository()
}
