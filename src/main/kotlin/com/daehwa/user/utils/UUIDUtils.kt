package com.daehwa.user.utils

import java.util.UUID

object UUIDUtils {
    fun generate(): String = UUID.randomUUID().toString()
}
