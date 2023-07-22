package com.reem.currencyconverter.data.mapper

import com.reem.currencyconverter.domain.entity.symbols.Symbols

fun mapSymbolsObjectToStringList(symbols: Symbols): MutableList<String> {
    val list = mutableListOf<String>()
    for (field in symbols.javaClass.declaredFields) {
        list.add(field.name)
    }
    return list
}