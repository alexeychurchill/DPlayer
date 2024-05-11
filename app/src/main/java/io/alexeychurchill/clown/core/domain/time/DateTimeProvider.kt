package io.alexeychurchill.clown.core.domain.time

import java.time.LocalDateTime

interface DateTimeProvider {

    fun current(): LocalDateTime
}
