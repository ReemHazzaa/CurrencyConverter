package com.reem.currencyconverter.app.adapters.otherCurrencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reem.currencyconverter.app.models.historicalData.RateUiItem
import com.reem.currencyconverter.databinding.ItemOtherCurrenciesBinding

class OtherCurrenciesAdapter : RecyclerView.Adapter<OtherCurrenciesAdapter.CustomViewHolder>() {

    private var data = emptyList<RateUiItem>()

    class CustomViewHolder(private val binding: ItemOtherCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rateUiItem: RateUiItem) {
            binding.item = rateUiItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOtherCurrenciesBinding.inflate(layoutInflater, parent, false)
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

    fun setData(newList: List<RateUiItem>) {
        val otherCurrenciesDiffUtil = OtherCurrenciesDiffUtil(data, newList)
        val diffUtilResult = DiffUtil.calculateDiff(otherCurrenciesDiffUtil)
        data = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}