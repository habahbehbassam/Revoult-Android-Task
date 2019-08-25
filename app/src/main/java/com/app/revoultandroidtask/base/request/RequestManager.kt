package com.app.revoultandroidtask.base.request

import com.app.revoultandroidtask.BuildConfig
import com.app.revoultandroidtask.BuildConfig.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
class RequestManager {


    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient

    companion object {
        private const val READ_TIME_OUT: Long = 120
        private const val WRITE_TIME_OUT: Long = 120
        private const val CONNECTION_TIME_OUT: Long = 3

        private var INSTANCE: RequestManager? = null
        val instance: RequestManager
            get() {
                if (INSTANCE == null) {
                    INSTANCE = RequestManager()
                }
                return INSTANCE!!
            }
    }

    init {
        createRetrofit(API_BASE_URL)
    }

    private fun createRetrofit(baseUrl: String) {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        okHttpClient = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    fun getRetrofit(): Retrofit {
        return retrofit
    }
}

