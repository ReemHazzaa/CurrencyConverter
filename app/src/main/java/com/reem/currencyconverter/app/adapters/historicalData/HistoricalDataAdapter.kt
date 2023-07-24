package com.reem.currencyconverter.app.adapters.historicalData

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reem.currencyconverter.app.models.historicalData.HistoricalDayUI
import com.reem.currencyconverter.databinding.ItemHistoricalDataBinding

class HistoricalDataAdapter : RecyclerView.Adapter<HistoricalDataAdapter.CustomViewHolder>() {

    private var data = emptyList<HistoricalDayUI>()

    class CustomViewHolder(private val binding: ItemHistoricalDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: HistoricalDayUI) {
            binding.item = day
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHistoricalDataBinding.inflate(layoutInflater, parent, false)
                return CustomViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder.from(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = data[position]
        holder.bind(currentItem)
    }

    fun setData(daysList: List<HistoricalDayUI>) {
        val historicalDataDiffUtil = HistoricalDataDiffUtil(data, daysList)
        val diffUtilResult = DiffUtil.calculateDiff(historicalDataDiffUtil)
        data = daysList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}