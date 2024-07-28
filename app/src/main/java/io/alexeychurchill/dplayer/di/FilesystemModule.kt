package io.alexeychurchill.dplayer.di

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.core.data.filesystem.DocumentFileFactory
import io.alexeychurchill.dplayer.core.data.filesystem.ForFile
import io.alexeychurchill.dplayer.core.data.filesystem.ForTree
import io.alexeychurchill.dplayer.library.data.filesystem.SafDirectoryPermissionsDispatcher
import io.alexeychurchill.dplayer.library.domain.DirectoryPermissionsDispatcher

@InstallIn(SingletonComponent::class)
@Module
abstract class FilesystemModule {

    companion object {

        @ForTree
        @Provides
        fun provideTreeDocumentFileFactory(
            @ApplicationContext context: Context
        ) = DocumentFileFactory { path -> DocumentFile.fromTreeUri(context, Uri.parse(path)) }

        @ForFile
        @Provides
        fun provideFileDocumentFileFactory(
            @ApplicationContext context: Context
        ) = DocumentFileFactory { path -> DocumentFile.fromSingleUri(context, Uri.parse(path)) }
    }

    @Binds
    abstract fun bindDirectoryPermissionsDispatcher(
        impl: SafDirectoryPermissionsDispatcher
    ): DirectoryPermissionsDispatcher
}
