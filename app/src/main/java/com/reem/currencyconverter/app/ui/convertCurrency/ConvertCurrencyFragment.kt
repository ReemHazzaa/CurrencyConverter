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
import androidx.navigation.fragment.findNavController
import com.reem.currencyconverter.R
import com.reem.currencyconverter.app.extensions.afterTextChanged
import com.reem.currencyconverter.app.extensions.makeInVisible
import com.reem.currencyconverter.app.extensions.makeVisible
import com.reem.currencyconverter.app.extensions.showGeneralDialog
import com.reem.currencyconverter.app.extensions.toast
import com.reem.currencyconverter.data.mapper.convertCurrency
import com.reem.currencyconverter.data.mapper.mapSymbolsObjectToStringList
import com.reem.currencyconverter.app.base.UiState
import com.reem.currencyconverter.app.extensions.updateText
import com.reem.currencyconverter.databinding.FragmentConvertCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment(), AdapterView.OnItemSelectedListener {
    /**
     * This flag is set to true while Swapping to stop selectedItemListener
     * on spinnerTo Since we only need the listener on spinnerFrom to
     * work in this case.
     */
    private var isSwapping: Boolean = false

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
            buttonDetails.setOnClickListener {
                val from = spinnerFrom.selectedItem.toString()
                val to = spinnerTo.selectedItem.toString()
                val action = ConvertCurrencyFragmentDirections.actionConvertCurrencyFragmentToHistoricalDataFragment()
                findNavController().navigate(action)
            }

            ivSwap.setOnClickListener {
//                swapFromAndToFields()
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
        setTextWatchers()
    }

    private fun setTextWatchers() {
        binding.etFrom.afterTextChanged {
            convertFromBaseToTargetCurrency(
                fromCurrency = selectedFromSymbol,
                toCurrency = selectedToSymbol,
                fromAmount = it,
                toEditText = binding.etTo
            )
        }

        //TODO: Reverse conversion, Issue -> infinite looping
//            etTo.afterTextChanged {
//
//            }
    }

    private fun swapFromAndToFields() {
        val from = binding.etFrom.text.toString()
        val to = binding.etTo.text.toString()
        if (from.isBlank() || to.isBlank()) {
            this.requireActivity()
                .toast(getString(R.string.from_to_fields_must_not_be_empty_for_swapping))
        } else {
            binding.apply {
                etFrom.setText(to)
                etTo.setText(from)
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
            isSwapping = true
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
                is UiState.Success -> {
                    hideLoading()
                    response.data?.symbols?.let {
                        mapSymbolsObjectToStringList(it)
                    }.also {
                        populateSpinnersWithSymbols(it)
                    }
                }

                is UiState.Error -> {
                    hideLoading()
                    context?.showGeneralDialog(
                        title = getString(R.string.error),
                        description = response.message.toString(),
                        onClickListener = null
                    )
                }

                is UiState.Loading -> showLoading()
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
                    if (!isSwapping) {
                        selectedToSymbol = binding.spinnerTo.selectedItem.toString()
                        if (selectedFromSymbol.isBlank()) selectedFromSymbol =
                            binding.spinnerFrom.selectedItem.toString()
                        convertFromBaseToTargetCurrency(
                            fromCurrency = selectedFromSymbol,
                            toCurrency = selectedToSymbol,
                            fromAmount = binding.etFrom.text.toString().ifBlank { "1" },
                            toEditText = binding.etTo,
                        )
                        isSwapping = false
                    }
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
                    is UiState.Success -> {
                        hideLoading()
                        response.data?.rates?.let {
                            val toConvertedValue = convertCurrency(fromAmount, it)
                            toEditText.updateText(toConvertedValue)
                        }
                    }

                    is UiState.Error -> {
                        hideLoading()
                        context?.showGeneralDialog(
                            title = getString(R.string.error),
                            description = response.message.toString(),
                            onClickListener = null
                        )
                    }

                    is UiState.Loading -> showLoading()
                }
            }
        }
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