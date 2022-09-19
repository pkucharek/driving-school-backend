package com.kucharek.drivingschoolbackend.account.activation

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

interface BaseUrlResolver {
    fun resolve(): String
}

class SpringBaseUrlResolver(
    @Value("frontend-application-base-path") val frontendApplicationBasePath: String,
): BaseUrlResolver {
    override fun resolve(): String {
        return frontendApplicationBasePath
    }
}

class MockBaseUrlResolver: BaseUrlResolver {
    override fun resolve(): String {
        return "http://localhost:3000"
    }
}
