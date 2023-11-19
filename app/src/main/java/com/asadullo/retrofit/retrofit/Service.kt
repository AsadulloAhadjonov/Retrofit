package com.asadullo.retrofit.retrofit

import com.asadullo.retrofit.models.MyData
import com.asadullo.retrofit.models.post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface Service {
    @GET("plan")
    fun get():Call<List<MyData>>

    @POST("plan/")
    fun add(@Body post:post):Call<MyData>

    @DELETE("plan/{id}/")
    fun delete(@Path("id") id:Int):Call<Int>

    @PATCH("plan/{id}/")
    fun uptade(@Path("id") id: Int, @Body post: post):Call<MyData>
}