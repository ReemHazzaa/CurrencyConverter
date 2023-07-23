package com.reem.currencyconverter.domain.useCase.historicalData

import com.reem.currencyconverter.domain.entity.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import com.reem.currencyconverter.domain.useCase.baseUseCase.BaseUseCase
import retrofit2.Response
import javax.inject.Inject

class GetHistoricalDataUseCase @Inject constructor(private val appRepo: AppRepo) :
    BaseUseCase<GetHistoricalDataUseCase.Params, Response<HistoricalDataResponse>>() {

    data class Params(
        val date: String,
        val base: String,
        val symbol: String
    )

    override suspend fun execute(params: Params?): Response<HistoricalDataResponse> {
        return appRepo.getHistoricalData(
            base = params!!.base,
            symbol = params.symbol,
            date = params.date
        )
    }

}