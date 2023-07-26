package com.reem.currencyconverter.domain.useCase.ratesUseCase

import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import com.reem.currencyconverter.domain.useCase.baseUseCase.BaseUseCase
import retrofit2.Response
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(private val appRepo: AppRepo) :
    BaseUseCase<GetRatesUseCase.Params, Response<RatesResponse>> {

    data class Params(
        val base: String,
        val commaSeparatedSymbols: String
    )

    override suspend fun execute(params: Params?): Response<RatesResponse> {
        return appRepo.getRates(
            base = params?.base ?: "EUR",
            commaSeparatedSymbols = params?.commaSeparatedSymbols ?: "EGP"
        )
    }

}