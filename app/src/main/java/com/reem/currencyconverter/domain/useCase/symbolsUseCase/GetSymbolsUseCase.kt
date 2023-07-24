package com.reem.currencyconverter.domain.useCase.symbolsUseCase

import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.repo.AppRepo
import com.reem.currencyconverter.domain.useCase.baseUseCase.BaseUseCase
import retrofit2.Response
import javax.inject.Inject

class GetSymbolsUseCase @Inject constructor(private val appRepo: AppRepo) :
    BaseUseCase<Any, Response<SymbolsResponse>>() {
    override suspend fun execute(params: Any?): Response<SymbolsResponse> {
       return appRepo.getSymbols()
    }
}