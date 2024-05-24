package io.alexeychurchill.clown.library.domain

interface DirectoryPermissionsDispatcher {

    suspend fun takePermissions(uri: String)

    suspend fun releasePermissions(uri: String)
}
