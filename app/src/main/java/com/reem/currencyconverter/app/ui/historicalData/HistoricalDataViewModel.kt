package com.reem.currencyconverter.app.ui.historicalData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reem.currencyconverter.R
import com.reem.currencyconverter.app.base.UiState
import com.reem.currencyconverter.app.models.historicalData.HistoricalDataUI
import com.reem.currencyconverter.data.mappers.mapHistoricalApiResponsesToUI
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.useCase.historicalData.GetHistoricalUseCase
import com.reem.currencyconverter.domain.useCase.ratesUseCase.GetRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoricalDataViewModel @Inject constructor(
    private val getHistoricalUC: GetHistoricalUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    private val networkManager: NetworkManager,
    private val application: Application
) : AndroidViewModel(application) {

    var historicalUI: MutableLiveData<UiState<HistoricalDataUI>> = MutableLiveData()
    fun performNetworkCallsConcurrently(
        datesList: List<String>,
        base: String,
        symbol: String,
        otherCurrenciesSymbols: String
    ) {
        historicalUI.value = UiState.Loading()
        if (networkManager.isNetworkAvailable()) {

            try {
                val firstDayDeferred = viewModelScope.async {
                    getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[0], base, symbol))
                }

                val secondDayDeferred = viewModelScope.async {
                    getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[1], base, symbol))
                }

                val thirdDayDeferred = viewModelScope.async {
                    getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[2], base, symbol))
                }

                val otherCurrenciesDeferred = viewModelScope.async {
                    getRatesUseCase.execute(GetRatesUseCase.Params(base, otherCurrenciesSymbols))
                }

                viewModelScope.launch {
                    val firstDayValue = firstDayDeferred.await()
                    val secondDayValue = secondDayDeferred.await()
                    val thirdDayValue = thirdDayDeferred.await()
                    val otherCurrencies = otherCurrenciesDeferred.await()

                    historicalUI.value =
                        handleNetworkResponses(
                            firstDayValue,
                            secondDayValue,
                            thirdDayValue,
                            otherCurrencies
                        )
                }
            } catch (e: Exception) {
                historicalUI.value =
                    UiState.Error(application.getString(R.string.request_is_not_successful))
            }

        } else {
            historicalUI.value =
                UiState.Error(application.getString(R.string.no_internet_connection))
        }
    }

    private fun handleNetworkResponses(
        firstDayValue: Response<HistoricalDataResponse>,
        secondDayValue: Response<HistoricalDataResponse>,
        thirdDayValue: Response<HistoricalDataResponse>,
        otherCurrencies: Response<RatesResponse>
    ): UiState<HistoricalDataUI> {
        return when {
            !firstDayValue.isSuccessful ||
                    !secondDayValue.isSuccessful ||
                    !thirdDayValue.isSuccessful ||
                    !otherCurrencies.isSuccessful ->
                UiState.Error(application.getString(R.string.request_is_not_successful))


            firstDayValue.code() != 200 ||
                    secondDayValue.code() != 200 ||
                    thirdDayValue.code() != 200 ||
                    otherCurrencies.code() != 200 ->
                UiState.Error(application.getString(R.string.request_is_not_successful))

            (firstDayValue.code() == 200 && firstDayValue.body()?.success == false) ||
                    (secondDayValue.code() == 200 && secondDayValue.body()?.success == false) ||
                    (thirdDayValue.code() == 200 && thirdDayValue.body()?.success == false) ||
                    (otherCurrencies.code() == 200 && otherCurrencies.body()?.success == false) -> {
                UiState.Error(
                    firstDayValue.body()?.error?.info
                        ?: secondDayValue.body()?.error?.info
                        ?: thirdDayValue.body()?.error?.info
                        ?: otherCurrencies.body()?.error?.info
                        ?: firstDayValue.body()?.error?.type?.replace("_", " ")
                        ?: secondDayValue.body()?.error?.type?.replace("_", " ")
                        ?: thirdDayValue.body()?.error?.type?.replace("_", " ")
                        ?: otherCurrencies.body()?.error?.type?.replace("_", " ")
                        ?: application.getString(R.string.unidentified_error)
                )
            }

            (firstDayValue.code() == 200 && firstDayValue.body()?.success == true) &&
                    (secondDayValue.code() == 200 && secondDayValue.body()?.success == true) &&
                    (thirdDayValue.code() == 200 && thirdDayValue.body()?.success == true) &&
                    (otherCurrencies.code() == 200 && otherCurrencies.body()?.success == true) -> {
                val uiResult = mapHistoricalApiResponsesToUI(
                    firstDayValue,
                    secondDayValue,
                    thirdDayValue,
                    otherCurrencies
                )

                return UiState.Success(uiResult)
            }

            else -> UiState.Error(application.getString(R.string.unidentified_error))
        }
    }
}