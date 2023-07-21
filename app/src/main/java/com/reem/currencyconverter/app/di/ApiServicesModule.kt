package com.reem.currencyconverter.app.di

import com.reem.currencyconverter.data.remote.apiService.FixerApiService
import com.reem.currencyconverter.data.remote.networkLayer.client.FixerClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServicesModule {

    @Provides
    @Singleton
    fun provideFixerApiService(fixerClient: FixerClient): FixerApiService {
        return fixerClient.build().create(FixerApiService::class.java)
    }

}