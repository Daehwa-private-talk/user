package com.daehwa.user.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    companion object {
        const val KEY = "Authorization"
    }

    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI()
            .info(
                Info()
                    .title("대화 API")
                    .version("1.0.0")
                    .description("Daehwa Auth API Description")
            )
            .addServersItem(Server().url(""))
            .addSecurityItem(SecurityRequirement().addList(KEY))
            .components(getComponent())
    }

    private fun getComponent() =
        Components()
            .addSecuritySchemes(
                KEY, SecurityScheme()
                    .name(KEY)
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)
            )
}
