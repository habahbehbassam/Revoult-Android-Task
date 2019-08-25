package com.app.revoultandroidtask.data.rate

import com.app.revoultandroidtask.common.ApiConstants.Companion.LATEST_API
import com.app.revoultandroidtask.models.rate.RateListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
interface RateApiMethods {

    @GET(LATEST_API)
    fun getLatestRates(@Query("base") base: String): Call<RateListModel>
}