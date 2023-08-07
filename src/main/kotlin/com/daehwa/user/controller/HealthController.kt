package com.daehwa.user.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE], name = "/api/v1")
@Hidden
class HealthController {
    @GetMapping("/health")
    fun getHealth(): ResponseEntity<Boolean> {
        return ResponseEntity.ok(true)
    }
}
