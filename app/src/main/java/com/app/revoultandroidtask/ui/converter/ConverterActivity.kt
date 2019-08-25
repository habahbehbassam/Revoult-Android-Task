package com.app.revoultandroidtask.ui.converter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.revoultandroidtask.R
import com.app.revoultandroidtask.databinding.ActivityConverterBinding
import com.app.revoultandroidtask.models.rate.RateModel
import com.app.revoultandroidtask.ui.converter.adapter.CurrencyConverterAdapter
import com.app.revoultandroidtask.ui.converter.adapter.OnAmountChangedListener
import com.app.revoultandroidtask.ui.converter.viewmodel.ConverterViewModel
import com.app.revoultandroidtask.ui.converter.viewmodel.ConverterViewModel.ConverterViewModelListener

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
class ConverterActivity : AppCompatActivity(), ConverterViewModelListener, OnAmountChangedListener {

    private lateinit var binding: ActivityConverterBinding
    private lateinit var viewModel: ConverterViewModel
    private val adapter by lazy { CurrencyConverterAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_converter)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setTitle(R.string.rate) // Change screen title

        setUpVM()
        setUpRecycler()
    }

    private fun setUpVM() {
        viewModel = ViewModelProvider(this).get(ConverterViewModel::class.java)
        viewModel.setCallback(this)
    }

    private fun setUpRecycler() {
        binding.rvCurrencies.layoutManager = LinearLayoutManager(this)
        binding.rvCurrencies.adapter = adapter

        adapter.setAmountListener(this)
    }

    override fun onAmountChanged(symbol: String, amount: Float) {
        viewModel.checkRates(symbol, amount)
    }

    override fun updateAmount(amount: Float) {
        super.updateAmount(amount)
        adapter.updateAmount(amount)
    }

    override fun onStarLoadRate() {
        super.onStarLoadRate()
        binding.pbLoading.visibility = View.VISIBLE
    }

    override fun onSuccessLoadRate(rates: ArrayList<RateModel>) {
        super.onSuccessLoadRate(rates)
        adapter.updateRates(rates)
        binding.pbLoading.visibility = View.GONE
    }

    override fun onFailedLoadRate() {
        super.onFailedLoadRate()
        binding.pbLoading.visibility = View.GONE
    }

}