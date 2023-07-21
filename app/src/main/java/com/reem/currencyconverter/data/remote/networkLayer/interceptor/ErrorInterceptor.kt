package com.reem.currencyconverter.data.remote.networkLayer.interceptor

import com.reem.currencyconverter.data.remote.networkLayer.NetworkFailure
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

class ErrorInterceptor : Interceptor {
    @Throws(NetworkFailure::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                return readErrorObject(response)
            }
            return response
        } catch (e: Exception) {
            if (e is NetworkFailure.ServerError)
                throw  e
            else
                throw NetworkFailure.NetworkError
        }
    }

    @Throws(NetworkFailure::class)
    private fun readErrorObject(response: Response): Response {
        response.peekBody(2048).string().let {
            try {
                val jsonObject = JSONObject(it)
                throw NetworkFailure.ServerError(jsonObject.getString("message"))
            } catch (e: JSONException) {
                throw NetworkFailure.NetworkError
            }
        }
    }
}
