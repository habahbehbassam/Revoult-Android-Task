package com.app.revoultandroidtask.base.request

/**
 * Created by Bassam Al.Habahbeh.
 * Habahbehbassam@gmail.com
 */
interface ResponseListener<MODEL> {

    fun onCallSuccess(successResponse: MODEL?)

    fun onCallFailed()
}