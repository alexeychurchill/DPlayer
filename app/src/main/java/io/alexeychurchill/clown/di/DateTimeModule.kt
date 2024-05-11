package io.alexeychurchill.clown.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alexeychurchill.clown.core.domain.time.DateTimeProvider
import io.alexeychurchill.clown.core.system.DefaultDateTimeProvider

@InstallIn(SingletonComponent::class)
@Module
abstract class DateTimeModule {

    @Binds
    abstract fun bindDateTimeProvider(impl: DefaultDateTimeProvider): DateTimeProvider
}
