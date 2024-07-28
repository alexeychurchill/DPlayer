package io.alexeychurchill.dplayer.core.system

import io.alexeychurchill.dplayer.core.domain.time.DateTimeProvider
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultDateTimeProvider @Inject constructor() : DateTimeProvider {

    override fun current(): LocalDateTime = LocalDateTime.now()
}
