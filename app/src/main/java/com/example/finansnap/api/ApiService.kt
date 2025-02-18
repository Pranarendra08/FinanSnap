package com.example.finansnap.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/ocr")
    fun snapOCR(
        @Part file: MultipartBody.Part
    ): Call<ResponseOCR>
}