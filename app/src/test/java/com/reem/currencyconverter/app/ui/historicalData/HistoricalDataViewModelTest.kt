package com.reem.currencyconverter.app.ui.historicalData

import com.reem.currencyconverter.data.repo.FakeAppRepoImpl
import com.reem.currencyconverter.domain.useCase.historicalData.GetHistoricalUseCase
import com.reem.currencyconverter.domain.useCase.ratesUseCase.GetRatesUseCase
import com.reem.currencyconverter.getOrAwaitValueTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HistoricalDataViewModelTest {

    private lateinit var viewModel: HistoricalDataViewModel
    private lateinit var fakeRepo: FakeAppRepoImpl

    @Before
    fun setUp() {
        fakeRepo = FakeAppRepoImpl()
        viewModel = HistoricalDataViewModel(
            GetHistoricalUseCase(fakeRepo),
            GetRatesUseCase(fakeRepo),
        )
    }

    @Test
    fun getHistorical_ValidInputs_success() {
        viewModel.getHistoricalAndOtherCurrencies(
            listOf("2023-07-22", "2023-07-21", "2023-07-20"),
            "EUR",
            "AED",
            "EGP,USD"
        )
        val value = viewModel.historicalUI.getOrAwaitValueTest()
        Assert.assertEquals(true, value.data!!.listOfHistoricalData.isNotEmpty())
        Assert.assertEquals(true, value.data!!.otherCurrencies.isNotEmpty())
    }

    @Test
    fun getHistorical_emptyDateListAndValidInputs_failure() {
        viewModel.getHistoricalAndOtherCurrencies(emptyList(), "EUR", "AED", "EGP")
        val value = viewModel.historicalUI.getOrAwaitValueTest()
        Assert.assertEquals(true, value.data!!.listOfHistoricalData.isEmpty())
    }

    @Test
    fun getHistorical_invalidBaseAndValidInputs_failure() {
        viewModel.getHistoricalAndOtherCurrencies(
            listOf("2023-07-22", "2023-07-21", "2023-07-20"),
            "EU",
            "AED",
            "EGP"
        )
        val value = viewModel.historicalUI.getOrAwaitValueTest()
        Assert.assertEquals(true, value.data!!.listOfHistoricalData.isEmpty())
        Assert.assertEquals(true, value.data!!.otherCurrencies.isNotEmpty())
    }

    @Test
    fun getHistorical_invalidHistoricalInputsAndValidOtherCurrencies_failure() {
        viewModel.getHistoricalAndOtherCurrencies(
            listOf("2023-07-22", "2023-07-21", "2023-07-20"),
            "EU",
            "AED",
            "EGP"
        )
        val value = viewModel.historicalUI.getOrAwaitValueTest()
        Assert.assertEquals(true, value.data!!.listOfHistoricalData.isEmpty())
        Assert.assertEquals(true, value.data!!.otherCurrencies.isNotEmpty())
    }

}