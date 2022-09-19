package com.kucharek.drivingschoolbackend.account

import java.util.*

data class AccountCreationResultDto(
    val result: AccountCreationResult,
    val accountId: UUID?,
    val message: String,
) {
    companion object {
        fun alreadyExistsByNationalIDNumber(nationalIdNumber: String) =
            AccountCreationResultDto(
                AccountCreationResult.NOT_CREATED,
                null,
                "Account with national ID number $nationalIdNumber" +
                        " already exists"
            )

        fun alreadyExistsByEmail(email: String) =
            AccountCreationResultDto(
                AccountCreationResult.NOT_CREATED,
                null,
                "Account with e-mail $email already exists"
            )

        fun created(accountId: UUID): AccountCreationResultDto =
            AccountCreationResultDto(AccountCreationResult.CREATED, accountId, "")
    }
}

enum class AccountCreationResult {
    CREATED,
    NOT_CREATED,
}
