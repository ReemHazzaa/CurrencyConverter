package com.reem.currencyconverter.domain.useCase.symbolsUseCase

import com.reem.currencyconverter.data.repo.FakeAppRepoImpl
import com.reem.currencyconverter.domain.models.symbols.SymbolsResponse
import com.reem.currencyconverter.domain.useCase.baseUseCase.BaseUseCase
import retrofit2.Response
import javax.inject.Inject

class GetSymbolsUseCaseTest @Inject constructor(private val appRepo: FakeAppRepoImpl) :
    BaseUseCase<Any, Response<SymbolsResponse>>() {
    override suspend fun execute(params: Any?): Response<SymbolsResponse> {
        return appRepo.getSymbols()
    }
}