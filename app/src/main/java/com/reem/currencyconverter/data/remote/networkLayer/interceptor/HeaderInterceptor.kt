package com.reem.currencyconverter.data.remote.networkLayer.interceptor

import com.reem.currencyconverter.data.remote.networkLayer.NetworkFailure
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to add headers to every request sent to API(Concept)
 */
class HeaderInterceptor @Inject constructor(private val networkManager: NetworkManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!networkManager.isNetworkAvailable()) {
            throw NetworkFailure.NetworkConnection
        }
        val request = chain.request()
        val builder = request.newBuilder()
        // To add headers to each request if needed, here we don't(Concept)
        builder.header("Content-Type", "application/json")
        return chain.proceed(builder.build())
    }

}