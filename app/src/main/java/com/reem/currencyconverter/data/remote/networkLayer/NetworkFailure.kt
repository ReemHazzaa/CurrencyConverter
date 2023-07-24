package com.reem.currencyconverter.data.remote.networkLayer

import java.io.IOException

sealed class NetworkFailure : IOException() {
    object NetworkConnection : NetworkFailure()
    object NetworkError : NetworkFailure()
    object UnknownError : NetworkFailure()
    data class ServerError(override val message: String) : NetworkFailure()
}