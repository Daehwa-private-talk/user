package com.daehwa.user.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig(
    private val property: JasyptProperty
) {
    companion object {
        const val JASYPT_ENCRYPTOR = "jasyptEncryptor"
    }

    @Bean(JASYPT_ENCRYPTOR)
    fun stringEncryptor(): StringEncryptor {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setPassword(property.encryptKey)
        encryptor.setAlgorithm(property.algorithm)
        return encryptor
    }
}
