package com.reem.currencyconverter.app.ui.convertCurrency

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reem.currencyconverter.R
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import com.reem.currencyconverter.app.base.UiState
import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.useCase.ratesUseCase.GetRatesUseCase
import com.reem.currencyconverter.domain.useCase.symbolsUseCase.GetSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val getSymbolsUseCase: GetSymbolsUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    private val networkManager: NetworkManager,
    private val application: Application
) : AndroidViewModel(application) {

    var symbolsResponse: MutableLiveData<UiState<SymbolsResponse>> = MutableLiveData()
    var ratesResponse: MutableLiveData<UiState<RatesResponse>> = MutableLiveData()

    fun getRates(from: String, to: String) = viewModelScope.launch {
        getRatesSafeCall(from, to)
    }

    private suspend fun getRatesSafeCall(from: String, to: String) {
        ratesResponse.value = UiState.Loading()
        if (networkManager.isNetworkAvailable()) {
            try {
                val response = getRatesUseCase.execute(
                    GetRatesUseCase.Params(
                        base = from,
                        commaSeparatedSymbols = to
                    )
                )
                ratesResponse.value = handleRatesResponse(response)
            } catch (e: Exception) {
                ratesResponse.value = UiState.Error(e.message.toString())
            }
        } else {
            ratesResponse.value = UiState.Error(application.getString(R.string.no_internet_connection))
        }
    }

    private fun handleRatesResponse(response: Response<RatesResponse>): UiState<RatesResponse>? {
        return when {
            !response.isSuccessful -> UiState.Error(application.getString(R.string.request_is_not_successful))

            response.code() != 200 -> UiState.Error(application.getString(R.string.request_is_not_successful))

            response.code() == 200 && response.body()?.success == false -> {
                UiState.Error(
                    response.body()?.error?.info
                        ?: response.body()?.error?.type?.replace("_", " ")
                        ?: application.getString(R.string.unidentified_error)
                )
            }

            response.code() == 200 && response.body()?.success == true -> {
                return UiState.Success(response.body())
            }

            else -> UiState.Error(response.message().toString())
        }
    }

    fun getSymbols() = viewModelScope.launch {
        getSymbolsSafeCall()
    }

    private suspend fun getSymbolsSafeCall() {
        symbolsResponse.value = UiState.Loading()
        if (networkManager.isNetworkAvailable()) {
            try {
                val response = getSymbolsUseCase.execute()
                symbolsResponse.value = handleSymbolsResponse(response)
            } catch (e: Exception) {
                symbolsResponse.value = UiState.Error(e.message.toString())
            }
        } else {
            symbolsResponse.value =
                UiState.Error(application.getString(R.string.no_internet_connection))
        }

    }

    private fun handleSymbolsResponse(response: Response<SymbolsResponse>)
            : UiState<SymbolsResponse> {
        return when {
            !response.isSuccessful -> UiState.Error(application.getString(R.string.request_is_not_successful))

            response.code() != 200 -> UiState.Error(application.getString(R.string.request_is_not_successful))

            response.code() == 200 && response.body()?.success == false -> {
                UiState.Error(
                    response.body()?.error?.info
                        ?: response.body()?.error?.type?.replace("_", " ")
                        ?: application.getString(R.string.unidentified_error)
                )
            }

            response.code() == 200 && response.body()?.success == true -> {
                return UiState.Success(response.body())
            }

            else -> UiState.Error(response.message().toString())
        }
    }

}