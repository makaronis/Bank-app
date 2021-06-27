package com.bank.app.di

import com.bank.app.data.api.CurrencyService
import com.bank.app.data.api.TransactionsService
import com.bank.app.data.repository.CardHoldersRepoImpl
import com.bank.app.data.repository.CurrencyRatesRepoImpl
import com.bank.app.domain.data.CurrencyProcessor
import com.bank.app.domain.repository.CardHoldersRepo
import com.bank.app.domain.repository.CurrencyRatesRepo
import com.bank.app.presentation.utils.AppConfig
import com.bank.bank_app.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier

@ExperimentalSerializationApi
@Module
@InstallIn(ViewModelComponent::class)
object AppProvideModule {

    @CurrencyRetrofit
    @Provides
    fun provideCurrencyRetrofit(): Retrofit = Retrofit.Builder().apply {
        baseUrl(AppConfig.CURRENCY_URL)
        addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    }.build()

    @TransactionsRetrofit
    @Provides
    fun provideTransactionsRetrofit(): Retrofit = Retrofit.Builder().apply {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                OkHttpProfilerInterceptor()
            )
        }
        client(builder.build())
        baseUrl(AppConfig.TRANSACTION_URL)
        addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    }.build()

    @Provides
    fun provideTransactionsService(@TransactionsRetrofit retrofit: Retrofit): TransactionsService =
        retrofit.create(TransactionsService::class.java)

    @Provides
    fun provideCurrencyService(@CurrencyRetrofit retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)

    @IoDispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Provides
    fun provideCurrencyProcessor() = CurrencyProcessor()

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppBindsModule {

    @Binds
    abstract fun bindCardHoldersRepo(impl: CardHoldersRepoImpl): CardHoldersRepo

    @Binds
    abstract fun bindCurrenciesRepo(impl: CurrencyRatesRepoImpl): CurrencyRatesRepo

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TransactionsRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher