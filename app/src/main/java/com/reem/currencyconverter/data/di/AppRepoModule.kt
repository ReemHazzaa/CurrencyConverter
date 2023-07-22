package com.reem.currencyconverter.data.di


import com.reem.currencyconverter.data.repo.AppRepoImpl
import com.reem.currencyconverter.domain.repo.AppRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppRepoModule {
    @Binds
    abstract fun provideAppRepoModule(appRepoImpl: AppRepoImpl): AppRepo
}