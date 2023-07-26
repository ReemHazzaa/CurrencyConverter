package com.reem.currencyconverter.app.ui.historicalData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.reem.currencyconverter.R
import com.reem.currencyconverter.app.adapters.historicalData.HistoricalDataAdapter
import com.reem.currencyconverter.app.adapters.otherCurrencies.OtherCurrenciesAdapter
import com.reem.currencyconverter.app.base.ErrorType
import com.reem.currencyconverter.app.base.UiState
import com.reem.currencyconverter.app.extensions.makeInVisible
import com.reem.currencyconverter.app.extensions.makeVisible
import com.reem.currencyconverter.app.extensions.showGeneralDialog
import com.reem.currencyconverter.app.models.historicalData.HistoricalDayUI
import com.reem.currencyconverter.app.models.historicalData.RateUiItem
import com.reem.currencyconverter.app.utils.getLast3daysDates
import com.reem.currencyconverter.data.mappers.getConversionRateValue
import com.reem.currencyconverter.databinding.FragmentHistoricalDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoricalDataFragment : Fragment() {
    private var _binding: FragmentHistoricalDataBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoricalDataViewModel by viewModels()

    private val args: HistoricalDataFragmentArgs by navArgs()

    private val historicalDataAdapter: HistoricalDataAdapter by lazy {
        HistoricalDataAdapter()
    }
    private val otherCurrencies = "USD,EUR,RUB,GBP,INR,JPY,MYR,EGP,AED,CNY"
    private val otherCurrenciesAdapter: OtherCurrenciesAdapter by lazy {
        OtherCurrenciesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricalDataBinding.inflate(inflater, container, false)
        val view = binding.root

        initUI()

        showLoading()
        loadData(getLast3daysDates(), args.fromSymbol, args.toSymbol, otherCurrencies)


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadData(
        datesList: List<String>,
        from: String,
        to: String,
        otherCurrencies: String
    ) {
        viewModel.performNetworkCallsConcurrently(datesList, from, to, otherCurrencies)
        viewModel.historicalUI.observe(viewLifecycleOwner) { response ->
            when (response) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> {
                    hideLoading()
                    populateHistoricalDataRV(response.data?.listOfHistoricalData)
                    populateChart(response.data?.listOfHistoricalData)
                    if (response.data?.otherCurrencies?.isNotEmpty() == true) {
                        populateCurrenciesRV(response.data.otherCurrencies)
                    }
                }

                is UiState.Error -> {
                    hideLoading()

                    val errorMessage = when (response.errorType) {
                        ErrorType.NO_INTERNET -> getString(R.string.no_internet_connection)
                        ErrorType.EXCEPTION -> response.message.toString()
                        ErrorType.UNKNOWN -> getString(R.string.unidentified_error)
                        ErrorType.API_ERROR -> getString(R.string.request_is_not_successful)
                        ErrorType.API_ERROR_WITH_MESSAGE -> response.message.toString()
                    }

                    context?.showGeneralDialog(
                        title = getString(R.string.error),
                        description = errorMessage,
                        onClickListener = null
                    )
                }
            }
        }
    }

    private fun populateCurrenciesRV(otherCurrencies: List<RateUiItem>) {
        otherCurrenciesAdapter.setData(otherCurrencies)
        binding.rvOtherCurrencies.adapter = otherCurrenciesAdapter
    }

    private fun populateHistoricalDataRV(list: List<HistoricalDayUI>?) {
        list?.let {
            historicalDataAdapter.setData(it)
            binding.rvHistoricalData.adapter = historicalDataAdapter
        }
    }

    private fun populateChart(list: List<HistoricalDayUI>?) {
        val chartEntryModel = entryModelOf(
            getConversionRateValue(list!![0].rates).toDouble(),
            getConversionRateValue(list[1].rates).toDouble(),
            getConversionRateValue(list[2].rates).toDouble()
        )
        binding.chartView.setModel(chartEntryModel)
    }

    private fun initUI() {
        binding.apply {
            tvHeader.text = buildString {
                append(getString(R.string.historical_data))
                append("\n")
                append(args.fromSymbol)
                append(" -> ")
                append(args.toSymbol)
            }
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = true
                loadData(getLast3daysDates(), args.fromSymbol, args.toSymbol, otherCurrencies)
                swipeRefresh.isRefreshing = false
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