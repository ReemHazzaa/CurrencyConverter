package com.reem.currencyconverter.app.di

import com.reem.currencyconverter.BuildConfig
import com.reem.currencyconverter.data.remote.networkLayer.interceptor.ErrorInterceptor
import com.reem.currencyconverter.data.remote.networkLayer.interceptor.HeaderInterceptor
import com.reem.currencyconverter.data.remote.networkLayer.interceptor.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor,
        headerInterceptor: HeaderInterceptor,
        errorInterceptor: ErrorInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(requestInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(okHttpLoggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }
    }
    @Singleton
    @Provides
    fun provideRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Singleton
    @Provides
    fun provideErrorInterceptor(): ErrorInterceptor {
        return ErrorInterceptor()
    }

}



