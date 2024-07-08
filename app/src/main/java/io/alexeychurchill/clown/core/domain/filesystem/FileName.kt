package io.alexeychurchill.clown.core.domain.filesystem

sealed interface FileName {

    companion object {

        const val DefaultUnknownValue = "-"

        fun of(name: String?): FileName = name?.let(FileName::Name) ?: Unknown
    }

    data object Unknown : FileName

    data class Name(val value: String) : FileName
}
