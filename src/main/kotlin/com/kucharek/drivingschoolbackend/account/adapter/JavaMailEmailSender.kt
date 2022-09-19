package com.kucharek.drivingschoolbackend.account.adapter

import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class JavaMailEmailSender(
    private val emailSender: JavaMailSender
) : EmailSenderPort {
    override fun sendActivationEmail(
        email: String,
        firstName: String,
        activationLink: String,
    ) {
        val message = SimpleMailMessage()

        message.setFrom("488a50f96e-097632@inbox.mailtrap.io")
        message.setSubject("Aktywacja konta")
        message.setText(generateEmailMessage(firstName, activationLink))
        message.setTo(email)

        emailSender.send(message)
    }
}
