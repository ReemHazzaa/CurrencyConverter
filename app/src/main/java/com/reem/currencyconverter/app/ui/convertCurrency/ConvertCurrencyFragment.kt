package com.reem.currencyconverter.app.ui.convertCurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reem.currencyconverter.app.extensions.makeInVisible
import com.reem.currencyconverter.app.extensions.makeVisible
import com.reem.currencyconverter.app.extensions.showGeneralDialog
import com.reem.currencyconverter.data.mapper.convertCurrency
import com.reem.currencyconverter.data.mapper.mapSymbolsObjectToStringList
import com.reem.currencyconverter.data.remote.networkLayer.NetworkResult
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment() {

    private var _binding: FragmentConvertCurrencyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConvertCurrencyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertCurrencyBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = true
                getSymbols()
                isRefreshing = false
            }
        }
        showLoading()
        getSymbols()


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun convertFromBaseToTargetCurrency(
        fromCurrency: String,
        toCurrency: String,
        fromAmount: String,
        fromEditText: EditText,
        toEditText: EditText
    ) {
        viewModel.getRates(fromCurrency, toCurrency)
        viewModel.ratesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoading()
                    response.data?.rates?.let {
                        val toConvertedValue = convertCurrency(fromAmount, it)
                        updateEditText(toConvertedValue, toEditText)
                    }
                }

                is NetworkResult.Error -> {
                    context?.showGeneralDialog(
                        title = getString(com.reem.currencyconverter.R.string.error),
                        description = response.message.toString(),
                        onClickListener = null
                    )
                }

                is NetworkResult.Loading -> showLoading()
            }
        }
    }

    private fun updateEditText(value: String, editText: EditText) {
        editText.setText(value)
    }

    private fun getSymbols() {
        viewModel.getSymbols()
        viewModel.symbolsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoading()
                    response.data?.symbols?.let {
                        mapSymbolsObjectToStringList(it)
                    }.also {
                        populateSpinnersWithSymbols(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    context?.showGeneralDialog(
                        title = getString(com.reem.currencyconverter.R.string.error),
                        description = response.message.toString(),
                        onClickListener = null
                    )
                }

                is NetworkResult.Loading -> showLoading()
            }
        }
    }

    private fun populateSpinnersWithSymbols(strings: MutableList<String>?) {
        ArrayAdapter(
            binding.spinnerFrom.context,
            android.R.layout.simple_spinner_item,
            strings!!.toTypedArray()
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFrom.adapter = spinnerAdapter
        }

        ArrayAdapter(
            binding.spinnerTo.context,
            android.R.layout.simple_spinner_item,
            strings.toTypedArray()
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTo.adapter = spinnerAdapter
        }
    }

    private fun showLoading() {
        binding.apply {
            clMain.makeInVisible()
            progressBar.makeVisible()
        }
    }

    private fun hideLoading() {
        binding.apply {
            clMain.makeVisible()
            progressBar.makeInVisible()
        }
    }
}