package com.kucharek.drivingschoolbackend.account.adapter

import com.kucharek.drivingschoolbackend.account.port.EmailSenderPort
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class ConsoleEmailSender() : EmailSenderPort {
    override fun sendActivationEmail(
        email: String,
        firstName: String,
        activationLink: String,
    ) {
        println(generateEmailMessage(firstName, activationLink))
    }
}
