package com.reem.currencyconverter.app.ui.convertCurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reem.currencyconverter.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_convert_currency, container, false)
    }
}