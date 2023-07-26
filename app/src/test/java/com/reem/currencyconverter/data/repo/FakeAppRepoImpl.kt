package com.reem.currencyconverter.data.repo

import com.reem.currencyconverter.domain.models.apiError.FixerApiError
import com.reem.currencyconverter.domain.models.historicalData.HistoricalDataResponse
import com.reem.currencyconverter.domain.models.rates.Rates
import com.reem.currencyconverter.domain.models.rates.RatesResponse
import com.reem.currencyconverter.domain.models.symbols.Symbols
import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import retrofit2.Response

class FakeAppRepoImpl : AppRepo {

    private var shouldReturnApiError: Boolean = false
    private val fixerApiError = FixerApiError(
        101,
        "invalid_access_key",
        "You have not supplied a valid API Access Key. [Technical Support: support@apilayer.com]"
    )

    fun setShouldReturnApiError(value: Boolean) {
        shouldReturnApiError = value
    }

    override suspend fun getSymbols(): Response<SymbolsResponse> {
        val successResponse =
            SymbolsResponse(success = true, symbols = Symbols(AED = "4.057732"), error = null)
        val errorResponse = SymbolsResponse(
            success = false,
            symbols = null,
            error = fixerApiError
        )
        return if (shouldReturnApiError) {
            Response.success(200, errorResponse)
        } else {
            Response.success(200, successResponse)
        }
    }

    override suspend fun getRates(
        base: String,
        commaSeparatedSymbols: String
    ): Response<RatesResponse> {
        val successResponse =
            RatesResponse(
                success = true,
                rates = Rates(AED = "4.057732"),
                base = "EUR",
                date = "2023-07-26",
                timestamp = 1690331528,
                error = null
            )
        val errorResponse = RatesResponse(
            success = false,
            rates = null,
            base = null,
            date = null,
            timestamp = null,
            error = fixerApiError
        )
        return if (shouldReturnApiError) {
            Response.success(200, errorResponse)
        } else {
            Response.success(200, successResponse)
        }
    }

    override suspend fun getHistoricalData(
        base: String,
        symbol: String,
        date: String
    ): Response<HistoricalDataResponse> {
        val successResponse = HistoricalDataResponse(
            success = true,
            rates = Rates(AED = "4.057732"),
            base = "EUR",
            date = "2023-07-26",
            timestamp = 1690331528,
            error = null,
            historical = true
        )
        val errorResponse = HistoricalDataResponse(
            success = false,
            rates = null,
            base = null,
            date = null,
            timestamp = null,
            error = fixerApiError,
            historical = null
        )
        return if (shouldReturnApiError) {
            Response.success(200, errorResponse)
        } else {
            Response.success(200, successResponse)
        }
    }
}