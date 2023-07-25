package com.reem.currencyconverter.app.ui.convertCurrency

import android.os.Bundle
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
import com.reem.currencyconverter.app.extensions.toast
import com.reem.currencyconverter.data.mapper.convertCurrency
import com.reem.currencyconverter.data.mapper.mapSymbolsObjectToStringList
import com.reem.currencyconverter.data.remote.networkLayer.NetworkResult
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var spinnerToAdapter: ArrayAdapter<String>
    private lateinit var spinnerFromAdapter: ArrayAdapter<String>
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
            etFrom.afterTextChanged {
                convertFromBaseToTargetCurrency(
                    fromCurrency = selectedFromSymbol,
                    toCurrency = selectedToSymbol,
                    fromAmount = it,
                    toEditText = binding.etTo
                )
            }
            etFrom.setText(getString(R.string._1))
            ivSwap.setOnClickListener {
                swapFromAndToSpinners()
            }

            spinnerFrom.onItemSelectedListener = this@ConvertCurrencyFragment
            spinnerTo.onItemSelectedListener = this@ConvertCurrencyFragment

            swipeRefresh.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    getSymbols()
                    isRefreshing = false
                }
            }
        }
    }

    private fun swapFromAndToSpinners() {
        if (selectedFromSymbol == selectedToSymbol) {
            this.requireActivity().toast(getString(R.string.same_symbols))
        } else if (selectedFromSymbol.isBlank() || selectedToSymbol.isBlank()) {
            this.requireActivity()
                .toast(getString(R.string.please_select_a_base_and_a_target_currency))
        } else {
            binding.apply {
                spinnerTo.setSelection(spinnerFromAdapter.getPosition(selectedFromSymbol), true)
                spinnerFrom.setSelection(spinnerToAdapter.getPosition(selectedToSymbol), true)

                selectedFromSymbol = spinnerFrom.selectedItem.toString()
                selectedToSymbol = spinnerTo.selectedItem.toString()
            }
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
        spinnerFromAdapter = ArrayAdapter(
            binding.spinnerFrom.context,
            android.R.layout.simple_spinner_item,
            strings!!.toTypedArray()
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFrom.adapter = spinnerAdapter
        }

        spinnerToAdapter = ArrayAdapter(
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
                    convertFromBaseToTargetCurrency(
                        fromCurrency = selectedFromSymbol,
                        toCurrency = selectedToSymbol,
                        fromAmount = binding.etFrom.text.toString(),
                        toEditText = binding.etTo,
                    )
                }
            }

            R.id.spinnerTo -> {
                if (++checkSpinnerTo > 1) {
                    selectedToSymbol = binding.spinnerTo.selectedItem.toString()
                    if (selectedFromSymbol.isBlank()) selectedFromSymbol =
                        binding.spinnerFrom.selectedItem.toString()
                    convertFromBaseToTargetCurrency(
                        fromCurrency = selectedFromSymbol,
                        toCurrency = selectedToSymbol,
                        fromAmount = binding.etFrom.text.toString().ifBlank { "1" },
                        toEditText = binding.etTo,
                    )
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
        toEditText: EditText
    ) {
        if (fromAmount.isNotBlank()) {
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