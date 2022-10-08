package com.kucharek.drivingschoolbackend.account.adapter

import com.kucharek.drivingschoolbackend.account.port.BaseUrlResolver

class MockBaseUrlResolver: BaseUrlResolver {
    override fun resolve(): String {
        return "http://localhost:3000"
    }
}
