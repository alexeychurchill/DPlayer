package io.alexeychurchill.clown.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.library.data.filesystem.SafDirectoryPermissionsDispatcher
import io.alexeychurchill.clown.library.domain.DirectoryPermissionsDispatcher

@InstallIn(SingletonComponent::class)
@Module
abstract class FilesystemModule {

    @Binds
    abstract fun bindDirectoryPermissionsDispatcher(
        impl: SafDirectoryPermissionsDispatcher
    ): DirectoryPermissionsDispatcher
}
