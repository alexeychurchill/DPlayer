package io.alexeychurchill.dplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.media.data.FileMetadataRepositoryImpl
import io.alexeychurchill.dplayer.media.domain.FileMetadataRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class FileMetadataModule {

    @Binds
    abstract fun bindFileMetadataRepository(
        impl: FileMetadataRepositoryImpl
    ): FileMetadataRepository
}
