package io.alexeychurchill.dplayer.core.domain.time

import java.time.LocalDateTime

interface DateTimeProvider {

    fun current(): LocalDateTime
}
