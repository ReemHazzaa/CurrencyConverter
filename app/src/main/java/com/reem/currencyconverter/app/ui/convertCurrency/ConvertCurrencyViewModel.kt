package com.reem.currencyconverter.app.ui.convertCurrency

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reem.currencyconverter.R
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import com.reem.currencyconverter.data.remote.networkLayer.NetworkResult
import com.reem.currencyconverter.domain.entity.symbols.Symbols
import com.reem.currencyconverter.domain.entity.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.useCase.symbolsUseCase.GetSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val getSymbolsUseCase: GetSymbolsUseCase,
    private val networkManager: NetworkManager,
    private val application: Application
) : AndroidViewModel(application) {

    var symbolsResponse: MutableLiveData<NetworkResult<SymbolsResponse>> = MutableLiveData()
    var symbolsLiveData: MutableLiveData<Symbols> = MutableLiveData()

    fun getSymbols() = viewModelScope.launch {
        getSymbolsSafeCall()
    }

    private suspend fun getSymbolsSafeCall() {
        symbolsResponse.value = NetworkResult.Loading()
        if (networkManager.isNetworkAvailable()) {
            try {
                val response = getSymbolsUseCase.execute()
                symbolsResponse.value = handleResponse(response)
            } catch (e: Exception) {
                symbolsResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            symbolsResponse.value =
                NetworkResult.Error(application.getString(R.string.no_internet_connection))
        }

    }

    private fun handleResponse(response: Response<SymbolsResponse>)
            : NetworkResult<SymbolsResponse> {
        return when {
            !response.isSuccessful -> NetworkResult.Error(application.getString(R.string.request_is_not_successful))

            response.code() != 200 -> NetworkResult.Error(application.getString(R.string.request_is_not_successful))

            response.code() == 200 && response.body()?.success == false -> {
                NetworkResult.Error(response.body()?.error?.info.toString())
            }

            response.code() == 200 && response.body()?.success == true -> {
                return NetworkResult.Success(response.body())
            }

            else -> NetworkResult.Error(response.message().toString())
        }
    }

}