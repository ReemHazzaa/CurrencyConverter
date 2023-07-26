package com.reem.currencyconverter.domain.useCase.historicalData

import com.reem.currencyconverter.data.repo.FakeAppRepoImpl
import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import com.reem.currencyconverter.domain.useCase.baseUseCase.BaseUseCase
import retrofit2.Response
import javax.inject.Inject

class GetHistoricalUseCase @Inject constructor(private val appRepo: FakeAppRepoImpl) :
    BaseUseCase<GetHistoricalUseCase.Params, Response<HistoricalDataResponse>>() {

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