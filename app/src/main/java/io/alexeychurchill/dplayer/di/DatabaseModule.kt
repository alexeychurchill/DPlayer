package io.alexeychurchill.dplayer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.library.data.database.DirectoryDao
import io.alexeychurchill.dplayer.library.data.database.LibraryDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): LibraryDatabase = Room
            .databaseBuilder(
                context,
                LibraryDatabase::class.java,
                LibraryDatabase.NAME
            )
            .build()

        @Provides
        fun provideDirectoryDao(libraryDatabase: LibraryDatabase): DirectoryDao {
            return libraryDatabase.directoryDao()
        }
    }
}
