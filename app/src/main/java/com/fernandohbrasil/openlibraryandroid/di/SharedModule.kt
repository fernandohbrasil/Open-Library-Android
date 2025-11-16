package com.fernandohbrasil.openlibraryandroid.di

import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapper
import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedModule {

    @Binds
    abstract fun bindCoverUrlMapper(
        impl: CoverUrlMapperImpl,
    ): CoverUrlMapper
}
