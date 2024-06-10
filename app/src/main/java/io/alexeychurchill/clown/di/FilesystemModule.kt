package io.alexeychurchill.clown.di

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.core.data.filesystem.DocumentFileFactory
import io.alexeychurchill.clown.core.data.filesystem.ForFile
import io.alexeychurchill.clown.core.data.filesystem.ForTree
import io.alexeychurchill.clown.library.data.filesystem.SafDirectoryPermissionsDispatcher
import io.alexeychurchill.clown.library.domain.DirectoryPermissionsDispatcher

@InstallIn(SingletonComponent::class)
@Module
abstract class FilesystemModule {

    companion object {

        @ForTree
        fun provideTreeDocumentFileFactory(
            @ApplicationContext context: Context
        ): DocumentFileFactory {
            return { path ->
                DocumentFile.fromTreeUri(context, Uri.parse(path))
            }
        }

        @ForFile
        fun provideFileDocumentFileFactory(
            @ApplicationContext context: Context
        ): DocumentFileFactory {
            return { path ->
                DocumentFile.fromSingleUri(context, Uri.parse(path))
            }
        }
    }

    @Binds
    abstract fun bindDirectoryPermissionsDispatcher(
        impl: SafDirectoryPermissionsDispatcher
    ): DirectoryPermissionsDispatcher
}
