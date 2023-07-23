package com.reem.currencyconverter.app.ui.historicalData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reem.currencyconverter.R
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import com.reem.currencyconverter.databinding.FragmentHistoricalDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoricalDataFragment : Fragment() {
    private var _binding: FragmentHistoricalDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricalDataBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}