package com.kucharek.drivingschoolbackend.account.activation

data class ActivationResultDto (
    val result: ActivationResult,
) {
    companion object {
        fun activated() = ActivationResultDto(ActivationResult.ACCOUNT_ACTIVATED)
        fun alreadyUsed() = ActivationResultDto(ActivationResult.LINK_ALREADY_USED)
        fun notFound() = ActivationResultDto(ActivationResult.NOT_FOUND)
    }
}

enum class ActivationResult {
    ACCOUNT_ACTIVATED, LINK_ALREADY_USED, NOT_FOUND,
}
