package com.daehwa.user.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jasypt")
data class JasyptProperty(
    val encryptKey: String,
    val algorithm: String,
)
