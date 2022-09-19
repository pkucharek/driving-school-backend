package com.kucharek.drivingschoolbackend.account.port

interface EmailSenderPort {
    fun sendActivationEmail(
        email: String,
        firstName: String,
        activationLink: String
    )

    fun generateEmailMessage(
        firstName: String,
        activationLink: String
    ): String {
        return """
            Cześć $firstName,
            Witamy w naszym ośrodku szkolenia kierowców, kliknij w link $activationLink żeby aktywować konto.
        """.trimIndent()
    }
}
