package io.alexeychurchill.dplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.dplayer.media.media3.Media3PlaybackEngine
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine

@InstallIn(SingletonComponent::class) // TODO: Consider making this ViewModelComponent
@Module
abstract class PlaybackEngineModule {

    @Binds
    abstract fun bindPlaybackEngine(impl: Media3PlaybackEngine): PlaybackEngine
}
