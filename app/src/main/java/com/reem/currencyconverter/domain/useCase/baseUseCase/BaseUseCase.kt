package com.reem.currencyconverter.domain.useCase.baseUseCase

interface BaseUseCase<in Params, out Type> {
    suspend fun execute(params: Params? = null): Type
}