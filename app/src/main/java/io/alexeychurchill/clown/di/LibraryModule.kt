package io.alexeychurchill.clown.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.library.data.database.DirectoryDao
import io.alexeychurchill.clown.library.data.database.LibraryDatabase

@InstallIn(SingletonComponent::class)
@Module
abstract class LibraryModule {

    companion object {

        @Provides
        fun provideDirectoryDao(libraryDatabase: LibraryDatabase): DirectoryDao {
            return libraryDatabase.directoryDao()
        }
    }
}
