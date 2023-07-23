package com.reem.currencyconverter.app.ui.historicalData

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reem.currencyconverter.R
import com.reem.currencyconverter.app.base.UiState
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import com.reem.currencyconverter.domain.entity.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.useCase.historicalData.GetHistoricalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalDataViewModel @Inject constructor(
    private val getHistoricalUC: GetHistoricalUseCase,
    private val networkManager: NetworkManager,
    private val application: Application
) : AndroidViewModel(application) {

    var historicalResponse: MutableLiveData<UiState<HistoricalDataResponse>> = MutableLiveData()

    fun performNetworkCallsConcurrently(datesList: List<String>, base: String, symbol: String) {
        historicalResponse.value = UiState.Loading()
        if (networkManager.isNetworkAvailable()) {

            val firstDayDeferred = viewModelScope.async {
                getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[0], base, symbol))
            }

            val secondDayDeferred = viewModelScope.async {
                getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[1], base, symbol))
            }

            val thirdDayDeferred = viewModelScope.async {
                getHistoricalUC.execute(GetHistoricalUseCase.Params(datesList[2], base, symbol))
            }

            viewModelScope.launch {
                val firstDayValue = firstDayDeferred.await()
                val secondDayValue = secondDayDeferred.await()
                val thirdDayValue = thirdDayDeferred.await()

                Log.e("REEM", firstDayValue.body()!!.date + " " + secondDayValue.body()!!.date + " " + thirdDayValue.body()!!.date)
            }


        } else {
            historicalResponse.value =
                UiState.Error(application.getString(R.string.no_internet_connection))
        }
    }
}