package io.alexeychurchill.dplayer.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.media.data.CoverArtModelLoaderFactory
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import java.nio.ByteBuffer

@GlideModule
class DPlayerGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Cover art
        val coverArtFactory = EntryPointAccessors
            .fromApplication(context.applicationContext, DPlayerGlideModuleEntryPoint::class.java)
            .coverArtLoaderFactory()

        registry.prepend(
            CoverArtPath::class.java,
            ByteBuffer::class.java,
            coverArtFactory
        )
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DPlayerGlideModuleEntryPoint {
        fun coverArtLoaderFactory(): CoverArtModelLoaderFactory
    }
}
