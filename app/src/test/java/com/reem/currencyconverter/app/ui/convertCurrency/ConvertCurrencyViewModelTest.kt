package com.reem.currencyconverter.app.ui.convertCurrency

import com.reem.currencyconverter.app.base.ErrorType
import com.reem.currencyconverter.data.repo.FakeAppRepoImpl
import com.reem.currencyconverter.domain.useCase.ratesUseCase.GetRatesUseCase
import com.reem.currencyconverter.domain.useCase.symbolsUseCase.GetSymbolsUseCase
import com.reem.currencyconverter.getOrAwaitValueTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ConvertCurrencyViewModelTest {

    private lateinit var viewModel: ConvertCurrencyViewModel
    private lateinit var fakeRepo: FakeAppRepoImpl

    @Before
    fun setUp() {
        fakeRepo = FakeAppRepoImpl()
        viewModel = ConvertCurrencyViewModel(
            GetSymbolsUseCase(fakeRepo),
            GetRatesUseCase(fakeRepo),
        )
    }

    @Test
    fun getRates_invalidBaseAndValidSymbol_failure() {
        viewModel.getRates("EURR", "AED")
        val value = viewModel.ratesResponse.getOrAwaitValueTest()
        Assert.assertEquals(ErrorType.API_ERROR, value.errorType)
    }

    @Test
    fun getRates_validBaseAndInvalidSymbol_failure() {
        viewModel.getRates("EUR", "AE")
        val value = viewModel.ratesResponse.getOrAwaitValueTest()
        Assert.assertEquals(ErrorType.API_ERROR, value.errorType)
    }

    @Test
    fun getRates_invalidBaseAndInvalidSymbol_failure() {
        viewModel.getRates("EU", "AE")
        val value = viewModel.ratesResponse.getOrAwaitValueTest()
        Assert.assertEquals(ErrorType.API_ERROR, value.errorType)
    }

    @Test
    fun getRates_validBaseAndValidSymbol_success() {
        viewModel.getRates("EUR", "AED")
        val value = viewModel.ratesResponse.getOrAwaitValueTest()
        Assert.assertEquals(true, value.data!!.success)
    }

}