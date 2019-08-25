package com.app.revoultandroidtask.ui.converter.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.revoultandroidtask.R
import com.app.revoultandroidtask.databinding.RowCurrencyConverterBinding
import com.app.revoultandroidtask.models.rate.RateModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
class CurrencyConverterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val symbolPosition = ArrayList<String>()
    private val symbolRate = HashMap<String?, RateModel>()

    private var amount = 1.0F
    private var onAmountChangedListener: OnAmountChangedListener? = null


    fun setAmountListener(onAmountChangedListener: OnAmountChangedListener?) {
        this.onAmountChangedListener = onAmountChangedListener
    }

    /**
     * Update the rate of each currency
     */
    fun updateRates(rates: ArrayList<RateModel>) {
        if (symbolPosition.isEmpty()) {
            symbolPosition.addAll(rates.map { it.symbol })
        }

        for (rate in rates) {
            symbolRate[rate.symbol] = rate
        }

        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    fun updateAmount(amount: Float) {
        this.amount = amount

        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    /**
     * Returns the rate at the given position
     */
    private fun rateAtPosition(pos: Int): RateModel {
        return symbolRate[symbolPosition[pos]]!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(rateAtPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_currency_converter,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int = symbolPosition.size

    inner class ViewHolder(var binding: RowCurrencyConverterBinding) : RecyclerView.ViewHolder(binding.root) {

        var symbol: String = ""

        fun bind(rateModel: RateModel) {
            if (symbol != rateModel.symbol) {

                this.symbol = rateModel.symbol
                binding.currency = rateModel

                setUpCurrencyChangedListener()
                setUpCurrencyEditTextFocusListener()
            }
            if (!binding.etAmount.isFocused) {
                binding.etAmount.setText((rateModel.rate?.times(amount)).toString())
            }
        }

        private fun setUpCurrencyEditTextFocusListener() {
            binding.etAmount.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    return@OnFocusChangeListener
                }
                changeItemPosition()
            }
        }

        private fun changeItemPosition() {
            adapterPosition.takeIf { it > 0 }?.also { currentPosition ->

                // move item from its current position
                symbolPosition.removeAt(currentPosition).also {
                    //add item to the top
                    symbolPosition.add(0, it)
                }

                // moved item to position 0
                notifyItemMoved(currentPosition, 0)
            }
        }

        private fun setUpCurrencyChangedListener() {
            binding.etAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(input: CharSequence?, start: Int, befor: Int, count: Int) {
                    if (input.toString().isNotEmpty()) {
                        if (binding.etAmount.isFocused) {
                            onAmountChangedListener?.onAmountChanged(symbol, input.toString().toFloat())
                        }
                    }
                }
            })
        }
    }
}