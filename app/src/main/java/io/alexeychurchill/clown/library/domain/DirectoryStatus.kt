package io.alexeychurchill.clown.library.domain

sealed interface DirectoryStatus {

    data object Unknown : DirectoryStatus

    data object Unavailable : DirectoryStatus

    data object Empty : DirectoryStatus

    data object Available : DirectoryStatus
}
