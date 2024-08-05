package io.alexeychurchill.dplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.library.data.Base64OpenDirectoryPayloadCodec
import io.alexeychurchill.dplayer.library.data.FileSystemRepositoryImpl
import io.alexeychurchill.dplayer.library.data.LibraryRepositoryImpl
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.presentation.OpenDirectoryPayloadCodec

@InstallIn(SingletonComponent::class)
@Module
abstract class LibraryModule {

    @Binds
    abstract fun bindDirectoryRepository(impl: LibraryRepositoryImpl): LibraryRepository

    @Binds
    abstract fun bindFileSystemRepository(impl: FileSystemRepositoryImpl): FileSystemRepository

    @Binds
    abstract fun bindPathCodec(impl: Base64OpenDirectoryPayloadCodec): OpenDirectoryPayloadCodec
}
