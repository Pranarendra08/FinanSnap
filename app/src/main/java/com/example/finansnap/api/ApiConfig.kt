package com.example.finansnap.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS) // waktu timeout koneksi
            .writeTimeout(60, TimeUnit.SECONDS)  // waktu timeout penulisan
            .readTimeout(60, TimeUnit.SECONDS)   // waktu timeout pembacaan
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(" https://354f-114-124-212-2.ngrok-free.app") //link ngrok
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}