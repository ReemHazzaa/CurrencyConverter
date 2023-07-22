package com.reem.currencyconverter.data.mapper

import com.google.gson.Gson
import com.reem.currencyconverter.domain.entity.rates.Rates
import com.reem.currencyconverter.domain.entity.symbols.Symbols

fun mapSymbolsObjectToStringList(symbols: Symbols): MutableList<String> {
    val list = mutableListOf<String>()
    for (field in symbols.javaClass.declaredFields) {
        list.add(field.name)
    }
    return list
}

fun convertCurrency(fromAmount: String?, rates: Rates): String {
    val targetRate = getConversionRateValue(rates)
    return (fromAmount?.toDouble()
        ?.times(targetRate.toDouble()) ?: 0.0).toString()

}

private fun getConversionRateValue(rates: Rates): String {
    val gson = Gson()
    var ratesJson = gson.toJson(rates, Rates::class.java)
    ratesJson = ratesJson.toString().substringAfter(":")
    ratesJson = ratesJson.substring(1, ratesJson.length - 2)
    return ratesJson
}