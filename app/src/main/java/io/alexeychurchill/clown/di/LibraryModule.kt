package io.alexeychurchill.clown.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.library.data.DirectoryRepositoryImpl
import io.alexeychurchill.clown.library.domain.DirectoryRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class LibraryModule {

    @Binds
    abstract fun bindDirectoryRepository(impl: DirectoryRepositoryImpl): DirectoryRepository
}
