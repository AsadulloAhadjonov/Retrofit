package com.asadullo.retrofit.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {

    const val URL = "https://hvax.pythonanywhere.com/"

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
    }

    fun getService():Service{
        return getRetrofit().create(Service::class.java)
    }
}