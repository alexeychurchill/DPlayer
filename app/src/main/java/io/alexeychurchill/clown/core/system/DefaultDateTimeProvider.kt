package io.alexeychurchill.clown.core.system

import io.alexeychurchill.clown.core.domain.time.DateTimeProvider
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultDateTimeProvider @Inject constructor() : DateTimeProvider {

    override fun current(): LocalDateTime = LocalDateTime.now()
}
