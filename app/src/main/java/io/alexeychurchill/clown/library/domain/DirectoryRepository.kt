package io.alexeychurchill.clown.library.domain

import kotlinx.coroutines.flow.Flow

interface DirectoryRepository {

    val allDirectories: Flow<List<Directory>>

    suspend fun addDirectory(directory: Directory)
}
