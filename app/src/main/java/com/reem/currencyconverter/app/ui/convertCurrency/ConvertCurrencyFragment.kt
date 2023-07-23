package com.reem.currencyconverter.app.ui.convertCurrency

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reem.currencyconverter.R
import com.reem.currencyconverter.app.extensions.afterTextChanged
import com.reem.currencyconverter.app.extensions.makeInVisible
import com.reem.currencyconverter.app.extensions.makeVisible
import com.reem.currencyconverter.app.extensions.showGeneralDialog
import com.reem.currencyconverter.data.mapper.convertCurrency
import com.reem.currencyconverter.data.mapper.mapSymbolsObjectToStringList
import com.reem.currencyconverter.data.remote.networkLayer.NetworkResult
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentConvertCurrencyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConvertCurrencyViewModel by viewModels()

    private var selectedFromSymbol: String = "AED"
    private var selectedToSymbol: String = "AED"


    // Flags to stop automatic call of onSelectedItemListener on start
    private var checkSpinnerFrom: Int = 0
    private var checkSpinnerTo: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertCurrencyBinding.inflate(inflater, container, false)
        val view = binding.root
        initUI()

        showLoading()
        getSymbols()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initUI() {
        binding.apply {

            spinnerFrom.onItemSelectedListener = this@ConvertCurrencyFragment
            spinnerTo.onItemSelectedListener = this@ConvertCurrencyFragment

            swipeRefresh.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    getSymbols()
                    isRefreshing = false
                }
            }

            etFrom.afterTextChanged {
                Log.e("REEM", it)
                convertFromBaseToTargetCurrency(
                    fromCurrency = selectedFromSymbol,
                    toCurrency = selectedToSymbol,
                    fromAmount = it,
                    fromEditText = binding.etFrom,
                    toEditText = binding.etTo
                )
            }

//            etTo.afterTextChanged {
//
//            }
        }
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
                        title = getString(R.string.error),
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.spinnerFrom -> {
                if (++checkSpinnerFrom > 1) {
                    selectedFromSymbol = binding.spinnerFrom.selectedItem.toString()
                    if (selectedToSymbol.isBlank()) selectedToSymbol =
                        binding.spinnerTo.selectedItem.toString()
//                    convertFromBaseToTargetCurrency(
//                        fromCurrency = selectedFromSymbol,
//                        toCurrency = selectedToSymbol,
//                        fromAmount = binding.etFrom.text.toString(),
//                        toEditText = binding.etTo,
//                        fromEditText = binding.etFrom
//                    )
                }
            }

            R.id.spinnerTo -> {
                if (++checkSpinnerTo > 1) {
                    selectedToSymbol = binding.spinnerTo.selectedItem.toString()
                    if (selectedFromSymbol.isBlank()) selectedFromSymbol =
                        binding.spinnerFrom.selectedItem.toString()
//                    convertFromBaseToTargetCurrency(
//                        fromCurrency = selectedFromSymbol,
//                        toCurrency = selectedToSymbol,
//                        fromAmount = binding.etFrom.text.toString(),
//                        toEditText = binding.etTo,
//                        fromEditText = binding.etFrom
//                    )
                }
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
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

    private fun updateEditText(value: String, editText: EditText) {
        editText.setText(value)
    }

    private fun showLoading() {
        binding.apply {
            progressBar.makeVisible()
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressBar.makeInVisible()
        }
    }
}