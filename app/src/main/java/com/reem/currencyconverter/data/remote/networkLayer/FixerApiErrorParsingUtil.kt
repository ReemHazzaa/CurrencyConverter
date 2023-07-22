package com.reem.currencyconverter.data.remote.networkLayer

import android.app.Application
import com.reem.currencyconverter.R

fun parseServerErrorCodeIntoMessage(
    code: Int,
    application: Application
): NetworkResult<Any> {
    return when (code) {
        404 -> NetworkResult.Error(application.getString(R.string.api_error_404))
        101 -> NetworkResult.Error(application.getString(R.string.api_error_101))
        103 -> NetworkResult.Error(application.getString(R.string.api_error_103))
        104 -> NetworkResult.Error(application.getString(R.string.api_error_104))
        105 -> NetworkResult.Error(application.getString(R.string.api_error_105))
        106 -> NetworkResult.Error(application.getString(R.string.api_error_106))
        102 -> NetworkResult.Error(application.getString(R.string.api_error_102))

        201 -> NetworkResult.Error(application.getString(R.string.api_error_201))
        202 -> NetworkResult.Error(application.getString(R.string.api_error_202))
        301 -> NetworkResult.Error(application.getString(R.string.api_error_301))
        302 -> NetworkResult.Error(application.getString(R.string.api_error_302))
        403 -> NetworkResult.Error(application.getString(R.string.api_error_403))

        501 -> NetworkResult.Error(application.getString(R.string.api_error_501))
        502 -> NetworkResult.Error(application.getString(R.string.api_error_502))
        503 -> NetworkResult.Error(application.getString(R.string.api_error_503))
        504 -> NetworkResult.Error(application.getString(R.string.api_error_504))
        505 -> NetworkResult.Error(application.getString(R.string.api_error_505))
        else -> NetworkResult.Error(application.getString(R.string.unknown_error))
    }
}