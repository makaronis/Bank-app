package com.bank.app.di

import com.bank.app.data.api.TransactionsService
import com.bank.app.utils.AppConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Qualifier

@ExperimentalSerializationApi
@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @CurrencyRetrofit
    @Provides
    fun provideCurrencyRetrofit(): Retrofit = Retrofit.Builder().apply {
        baseUrl(AppConfig.CURRENCY_URL)
        addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    }.build()

    @TransactionsRetrofit
    @Provides
    fun provideTransactionsRetrofit(): Retrofit = Retrofit.Builder().apply {
        baseUrl(AppConfig.TRANSACTION_URL)
        addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    }.build()

    @Provides
    fun provideTransactionsService(@TransactionsRetrofit retrofit: Retrofit): TransactionsService =
        retrofit.create(TransactionsService::class.java)

    @Provides
    fun provideCurrencyService(@CurrencyRetrofit retrofit: Retrofit): TransactionsService =
        retrofit.create(TransactionsService::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TransactionsRetrofit