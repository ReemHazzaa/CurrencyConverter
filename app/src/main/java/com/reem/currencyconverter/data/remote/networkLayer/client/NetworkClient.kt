package com.reem.currencyconverter.data.remote.networkLayer.client

import retrofit2.Retrofit

/**
 * An abstract class of client to be extended for handling multiple base URls
 */
abstract class NetworkClient( private val retrofitBuilder: Retrofit.Builder){

    protected abstract val baseUrl: String

    fun build(): Retrofit {
        return retrofitBuilder.baseUrl(baseUrl).build()
    }
}