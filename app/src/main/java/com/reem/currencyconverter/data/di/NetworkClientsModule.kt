package com.reem.currencyconverter.data.di

import com.reem.currencyconverter.data.remote.networkLayer.client.FixerClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkClientsModule {

    @Provides
    @Singleton
    fun provideFixerNetworkClient(retrofitBuilder: Retrofit.Builder): FixerClient {
        return FixerClient(retrofitBuilder)
    }

}