package com.app.revoultandroidtask.data.rate

import android.content.Context
import com.app.revoultandroidtask.base.request.BaseAPI
import com.app.revoultandroidtask.models.rate.RateListModel
import retrofit2.Call

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
class RateDao(context: Context) : BaseAPI<RateApiMethods, RateListModel>(context, RateApiMethods::class.java) {

    private lateinit var base: String

    fun setParam(base: String): RateDao {
        this.base = base
        return this
    }

    override fun createCall(): Call<RateListModel> {
        return apiInterface?.getLatestRates(base)!!
    }
}