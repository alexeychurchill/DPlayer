package io.alexeychurchill.clown.library.domain

sealed interface FileName {

    companion object {
        fun of(name: String?): FileName =
            name?.let(::Name) ?: Unknown
    }

    data object Unknown : FileName

    data class Name(val value: String) : FileName
}
