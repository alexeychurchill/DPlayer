package io.alexeychurchill.dplayer.library.domain

interface DirectoryPermissionsDispatcher {

    suspend fun takePermissions(uri: String)

    suspend fun releasePermissions(uri: String)
}
