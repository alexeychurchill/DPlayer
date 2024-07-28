package io.alexeychurchill.clown.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.library.data.Base64PathCodec
import io.alexeychurchill.clown.library.data.FileSystemRepositoryImpl
import io.alexeychurchill.clown.library.data.LibraryRepositoryImpl
import io.alexeychurchill.clown.library.domain.FileSystemRepository
import io.alexeychurchill.clown.library.domain.LibraryRepository
import io.alexeychurchill.clown.library.domain.PathCodec

@InstallIn(SingletonComponent::class)
@Module
abstract class LibraryModule {

    @Binds
    abstract fun bindDirectoryRepository(impl: LibraryRepositoryImpl): LibraryRepository

    @Binds
    abstract fun bindFileSystemRepository(impl: FileSystemRepositoryImpl): FileSystemRepository

    @Binds
    abstract fun bindPathCodec(impl: Base64PathCodec): PathCodec
}
