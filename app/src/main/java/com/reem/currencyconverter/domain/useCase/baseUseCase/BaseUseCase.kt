package com.reem.currencyconverter.domain.useCase.baseUseCase
abstract class BaseUseCase<in Params, out Type> {
    abstract suspend fun execute(params: Params?): Type
}