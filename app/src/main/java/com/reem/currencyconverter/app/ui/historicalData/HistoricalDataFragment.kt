package com.reem.currencyconverter.app.ui.historicalData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reem.currencyconverter.R
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import com.reem.currencyconverter.databinding.FragmentHistoricalDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoricalDataFragment : Fragment() {
    private var _binding: FragmentHistoricalDataBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoricalDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricalDataBinding.inflate(inflater, container, false)
        val view = binding.root

//        viewModel.performNetworkCallsConcurrently(listOf("2022-07-20", "2022-07-21", "2022-07-22"), "EUR", "EGP")

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}