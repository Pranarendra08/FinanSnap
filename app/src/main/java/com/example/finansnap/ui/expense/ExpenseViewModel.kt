package com.example.finansnap.ui.expense

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finansnap.api.ApiConfig
import com.example.finansnap.api.OCRResult
import com.example.finansnap.api.ResponseOCR
import com.example.finansnap.util.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "ExpenseViewModel"
    }

    private val _resultOcr = MutableLiveData<OCRResult>()
    val resultOcr: LiveData<OCRResult> = _resultOcr

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun snapOCR(getFile: File) {
        _isLoading.value = true

        val file = reduceFileImage(getFile)

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )

        val apiService = ApiConfig.getApiService()
        val resultOCR = apiService.snapOCR(imageMultipart)
        resultOCR.enqueue(object : Callback<ResponseOCR> {
            override fun onResponse(call: Call<ResponseOCR>, response: Response<ResponseOCR>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _resultOcr.value = responseBody.ocrResult
                    }
                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "$errorBody")
                }
            }

            override fun onFailure(call: Call<ResponseOCR>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}