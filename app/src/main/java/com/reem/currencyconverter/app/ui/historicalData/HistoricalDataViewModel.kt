package com.reem.currencyconverter.app.ui.historicalData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.reem.currencyconverter.data.remote.networkLayer.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoricalDataViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val application: Application
) : AndroidViewModel(application) {
}