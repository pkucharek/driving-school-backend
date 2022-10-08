package com.kucharek.drivingschoolbackend.account.adapter

import com.kucharek.drivingschoolbackend.account.port.BaseUrlResolver
import org.springframework.beans.factory.annotation.Value

class SpringBaseUrlResolver(
    @Value("frontend-application-base-path") val frontendApplicationBasePath: String,
): BaseUrlResolver {
    override fun resolve(): String {
        return frontendApplicationBasePath
    }
}
