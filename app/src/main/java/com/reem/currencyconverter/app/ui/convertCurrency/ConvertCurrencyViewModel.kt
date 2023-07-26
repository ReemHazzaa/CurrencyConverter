package com.reem.currencyconverter.app.ui.convertCurrency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reem.currencyconverter.app.base.ErrorType
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
) : ViewModel() {

    private var _symbolsResponse: MutableLiveData<UiState<SymbolsResponse>> = MutableLiveData()
    val symbolsResponse: LiveData<UiState<SymbolsResponse>> = _symbolsResponse

    private var _ratesResponse: MutableLiveData<UiState<RatesResponse>> = MutableLiveData()
    val ratesResponse: LiveData<UiState<RatesResponse>> = _ratesResponse

    fun getRates(from: String, to: String) = viewModelScope.launch {
        _ratesResponse.value = UiState.Loading()
        try {
            val response = getRatesUseCase.execute(
                GetRatesUseCase.Params(
                    base = from,
                    commaSeparatedSymbols = to
                )
            )
            _ratesResponse.value = handleRatesResponse(response)
        } catch (e: Exception) {
            _ratesResponse.value = UiState.Error(ErrorType.EXCEPTION, e.message.toString())
        }
    }

    private fun handleRatesResponse(response: Response<RatesResponse>): UiState<RatesResponse> {
        return when {
            !response.isSuccessful -> UiState.Error(ErrorType.API_ERROR)

            response.code() != 200 -> UiState.Error(ErrorType.API_ERROR)

            response.code() == 200 && response.body()?.success == false -> {
                UiState.Error(
                    ErrorType.API_ERROR_WITH_MESSAGE,
                    response.body()?.error?.info
                        ?: response.body()?.error?.type?.replace("_", " ")
                )
            }

            response.code() == 200 && response.body()?.success == true -> {
                return UiState.Success(response.body())
            }

            else -> UiState.Error(ErrorType.UNKNOWN)
        }
    }

    fun getSymbols() = viewModelScope.launch {
        _symbolsResponse.value = UiState.Loading()
        try {
            val response = getSymbolsUseCase.execute()
            _symbolsResponse.value = handleSymbolsResponse(response)
        } catch (e: Exception) {
            _symbolsResponse.value = UiState.Error(ErrorType.EXCEPTION, e.message.toString())
        }
    }

    private fun handleSymbolsResponse(response: Response<SymbolsResponse>)
            : UiState<SymbolsResponse> {
        return when {
            !response.isSuccessful -> UiState.Error(ErrorType.API_ERROR)

            response.code() != 200 -> UiState.Error(ErrorType.API_ERROR)

            response.code() == 200 && response.body()?.success == false -> {
                UiState.Error(
                    ErrorType.API_ERROR_WITH_MESSAGE,
                    response.body()?.error?.info
                        ?: response.body()?.error?.type?.replace("_", " ")
                )
            }

            response.code() == 200 && response.body()?.success == true -> {
                return UiState.Success(response.body())
            }

            else -> UiState.Error(ErrorType.UNKNOWN)
        }
    }

}