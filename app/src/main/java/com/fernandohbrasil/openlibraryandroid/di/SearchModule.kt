package com.fernandohbrasil.openlibraryandroid.di

import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApi
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiService
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiServiceImpl
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapper
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapperImpl
import com.fernandohbrasil.openlibraryandroid.data.search.repository.SearchRepositoryImpl
import com.fernandohbrasil.openlibraryandroid.domain.search.repository.SearchRepository
import com.fernandohbrasil.openlibraryandroid.domain.search.usecase.GetBooks
import com.fernandohbrasil.openlibraryandroid.domain.search.usecase.GetBooksImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    companion object {
        @Provides
        @Singleton
        fun provideSearchApi(retrofit: Retrofit): SearchApi {
            return retrofit.create(SearchApi::class.java)
        }
    }

    @Binds
    abstract fun bindSearchApiService(
        searchApiServiceImpl: SearchApiServiceImpl,
    ): SearchApiService

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl,
    ): SearchRepository

    @Binds
    abstract fun bindNetworkToDomainBookSearchMapper(
        networkToDomainBookSearchMapperImpl: NetworkToDomainBookSearchMapperImpl,
    ): NetworkToDomainBookSearchMapper

    @Binds
    abstract fun bindGetBooks(
        getBooksImpl: GetBooksImpl,
    ): GetBooks
}
