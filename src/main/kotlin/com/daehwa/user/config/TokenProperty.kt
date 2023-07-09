package com.daehwa.user.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token")
data class TokenProperty(
    val secretKey: String,
    val accessTokenExpireTime: Long,
    val refreshTokenRenewHour: Long
)
