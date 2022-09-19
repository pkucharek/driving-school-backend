package com.kucharek.drivingschoolbackend.account

import com.kucharek.drivingschoolbackend.event.EventStore

class AccountCommandResolver(
    private val eventStore: EventStore<Account, AccountEvent>,
) {


    fun activateAccount() {

    }
}
