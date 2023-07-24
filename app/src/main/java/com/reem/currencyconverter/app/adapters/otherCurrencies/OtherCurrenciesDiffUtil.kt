package com.reem.currencyconverter.app.adapters.otherCurrencies

import androidx.recyclerview.widget.DiffUtil
import com.reem.currencyconverter.app.models.historicalData.RateUiItem

class OtherCurrenciesDiffUtil(
    private val oldList: List<RateUiItem>,
    private val newList: List<RateUiItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}