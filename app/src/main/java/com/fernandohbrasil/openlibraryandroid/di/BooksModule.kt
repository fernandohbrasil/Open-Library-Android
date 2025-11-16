package com.fernandohbrasil.openlibraryandroid.di

import com.fernandohbrasil.openlibraryandroid.data.books.datasource.BooksApi
import com.fernandohbrasil.openlibraryandroid.data.books.datasource.BooksApiService
import com.fernandohbrasil.openlibraryandroid.data.books.datasource.BooksApiServiceImpl
import com.fernandohbrasil.openlibraryandroid.data.books.mapper.NetworkToDomainBookDetailsMapper
import com.fernandohbrasil.openlibraryandroid.data.books.mapper.NetworkToDomainBookDetailsMapperImpl
import com.fernandohbrasil.openlibraryandroid.data.books.repository.BooksRepositoryImpl
import com.fernandohbrasil.openlibraryandroid.domain.books.repository.BooksRepository
import com.fernandohbrasil.openlibraryandroid.domain.books.usecase.GetBookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.usecase.GetBookDetailsImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksModule {

    companion object {
        @Provides
        @Singleton
        fun provideBooksApi(retrofit: Retrofit): BooksApi {
            return retrofit.create(BooksApi::class.java)
        }
    }

    @Binds
    abstract fun bindBooksApiService(
        booksApiServiceImpl: BooksApiServiceImpl,
    ): BooksApiService

    @Binds
    abstract fun bindNetworkToDomainBookDetailsMapper(
        networkToDomainBookDetailsMapperImpl: NetworkToDomainBookDetailsMapperImpl,
    ): NetworkToDomainBookDetailsMapper

    @Binds
    abstract fun bindBooksRepository(
        booksRepositoryImpl: BooksRepositoryImpl,
    ): BooksRepository

    @Binds
    abstract fun bindGetBookDetails(
        getBookDetailsImpl: GetBookDetailsImpl,
    ): GetBookDetails
}

