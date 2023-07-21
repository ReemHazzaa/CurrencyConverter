package com.reem.currencyconverter.data.remote.networkLayer.client


import com.reem.currencyconverter.BuildConfig
import retrofit2.Retrofit

class FixerClient(retrofitBuilder: Retrofit.Builder) : NetworkClient(retrofitBuilder) {

    override val baseUrl = BuildConfig.FIXER_API_BASE_URL
}