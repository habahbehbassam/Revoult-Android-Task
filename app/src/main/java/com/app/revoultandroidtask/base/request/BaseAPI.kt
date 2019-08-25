package com.app.revoultandroidtask.base.request

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
abstract class BaseAPI<INTERFACE, MODEL>(private var context: Context, service: Class<INTERFACE>) {

    private lateinit var mRetrofit: Retrofit
    protected var apiInterface: INTERFACE? = null
    private lateinit var listener: ResponseListener<MODEL>


    init {
        setUpRetrofit()
        apiInterface = mRetrofit.create(service)
    }

    private fun setUpRetrofit() {
        mRetrofit = RequestManager.instance.getRetrofit()
    }

    fun setListener(listener: ResponseListener<MODEL>): BaseAPI<INTERFACE, MODEL> {
        this.listener = listener
        return this
    }

    fun sendRequest(): Call<MODEL> {
        return request()
    }

    private fun request(): Call<MODEL> {
        val callObj = createCall()

        callObj.enqueue(object : Callback<MODEL> {
            override fun onResponse(call: Call<MODEL>, response: Response<MODEL>) {
                listener.onCallSuccess(response.body())
            }

            override fun onFailure(call: Call<MODEL>, t: Throwable) {
                listener.onCallFailed()
            }
        })

        return callObj
    }

    abstract fun createCall(): Call<MODEL>
}