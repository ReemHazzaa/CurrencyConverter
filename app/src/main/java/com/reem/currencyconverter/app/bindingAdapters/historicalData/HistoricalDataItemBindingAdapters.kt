package com.reem.currencyconverter.app.bindingAdapters.historicalData

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.reem.currencyconverter.app.utils.formatDate
import com.reem.currencyconverter.data.mappers.getConversionRateValue
import com.reem.currencyconverter.domain.models.rates.Rates

class HistoricalDataItemBindingAdapters {

    companion object {

        @JvmStatic
        @BindingAdapter("setHistoricalDateToTextView")
        fun setHistoricalDate(textView: TextView, unFormattedDate: String) {
            textView.text = formatDate(unFormattedDate)
        }

        @JvmStatic
        @BindingAdapter("setRateToTextView")
        fun setRate(textView: TextView, rates: Rates) {
            textView.text = getConversionRateValue(rates)
        }
    }

}