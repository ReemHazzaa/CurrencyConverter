package com.reem.currencyconverter.app.adapters.historicalData

import androidx.recyclerview.widget.DiffUtil
import com.reem.currencyconverter.app.models.historicalData.HistoricalDayUI

class HistoricalDataDiffUtil(
    private val oldList: List<HistoricalDayUI>,
    private val newList: List<HistoricalDayUI>,
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