package com.app.revoultandroidtask.ui.converter.viewmodel

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import com.app.revoultandroidtask.base.request.ResponseListener
import com.app.revoultandroidtask.data.rate.RateDao
import com.app.revoultandroidtask.models.rate.RateListModel
import com.app.revoultandroidtask.models.rate.RateModel
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by bassam Al.habahbeh on 2019-08-24.
 * Habahbehbassam@gmail.com
 */
class ConverterViewModel(application: Application) : AndroidViewModel(application) {


    companion object {
        const val DEFAULT_SYMBOL = "EUR"
        const val DEFAULT_MOUNT = 1.0f

        const val LOAD_CURRENCY_RATE_UPDATE_DELAY: Long = 0
        const val LOAD_CURRENCY_RATE_UPDATE_PERIOD: Long = 1000
    }

    private var listener: ConverterViewModelListener? = null
    private var currentBase: String = DEFAULT_SYMBOL
    private val rateDao: RateDao by lazy { RateDao(getApplication()) }
    private var rates: ArrayList<RateModel> = ArrayList()
    private var isLoaded = false

    init {
        val handler = Handler()

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post { getLatestCurrencyRate((currentBase)) }
            }
        }, LOAD_CURRENCY_RATE_UPDATE_DELAY, LOAD_CURRENCY_RATE_UPDATE_PERIOD)
    }

    fun setCallback(listener: ConverterViewModelListener) {
        this.listener = listener
    }

    fun checkRates(base: String = DEFAULT_SYMBOL, amount: Float = DEFAULT_MOUNT) {
        if (base.equals(currentBase, ignoreCase = true)) {
            listener?.updateAmount(amount)
        } else {
            getLatestCurrencyRate(base)
        }
    }

    private fun getLatestCurrencyRate(base: String) {
        if (!isLoaded) {
            listener?.onStarLoadRate()
        }

        currentBase = base.toUpperCase()
        rateDao.setParam(currentBase)
            .setListener(object : ResponseListener<RateListModel> {
                override fun onCallSuccess(successResponse: RateListModel?) {
                    rates.clear()
                    rates.add(RateModel(successResponse?.base!!, DEFAULT_MOUNT))
                    rates.addAll(successResponse.rates.map { RateModel(it.key, it.value) })
                    listener?.onSuccessLoadRate(rates)
                    isLoaded = true
                }

                override fun onCallFailed() {
                    listener?.onFailedLoadRate()
                }
            }).sendRequest()
    }

    interface ConverterViewModelListener {
        fun updateAmount(amount: Float) {

        }

        fun onStarLoadRate() {

        }

        fun onSuccessLoadRate(rates: ArrayList<RateModel>) {

        }

        fun onFailedLoadRate() {

        }

    }

}