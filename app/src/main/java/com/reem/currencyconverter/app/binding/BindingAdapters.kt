package com.reem.currencyconverter.app.binding

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.reem.currencyconverter.data.mapper.mapSymbolsObjectToStringList
import com.reem.currencyconverter.domain.entity.symbols.Symbols

object BindingAdapters {
    @BindingAdapter("populateSpinner")
    @JvmStatic
    fun populateSpinner(spinner: Spinner, symbols: Symbols?) {
        symbols?.let {
            val spinnerAdapter: ArrayAdapter<String> by lazy {
                ArrayAdapter(
                    spinner.context,
                    android.R.layout.simple_spinner_item,
                    mapSymbolsObjectToStringList(symbols)
                ).also { spinnerAdapter ->
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = spinnerAdapter
                }
            }
        }
    }
}