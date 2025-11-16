package com.example.employeeapp.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    fun getInstance(): ApiService{
        val  mHttpLoggingInterceptor = HttpLoggingInterceptor{msg -> Log.d("HTTP", msg) }
            .setLevel(
            HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .connectTimeout(2, TimeUnit.SECONDS)  // ✅ Tambah timeout
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

        val builder = Retrofit.Builder().baseUrl("https://dummy.restapiexample.com/api/v1/")
            .addConverterFactory(
            GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        return builder.create(ApiService::class.java)

    }
}