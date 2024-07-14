package com.example.finansnap

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.ui.detail.DetailViewModel
import com.example.finansnap.ui.input.TransaksiAddViewModel
import com.example.finansnap.ui.home.TransaksiViewModel

class DatabaseViewModelFactory private constructor(
    private val transaksiRepository: TransaksiRepository
) : ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var instance: DatabaseViewModelFactory? = null

        fun getInstanceData(context: Context): DatabaseViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DatabaseViewModelFactory(
                    TransaksiRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(transaksiRepository) as T
        } else if (modelClass.isAssignableFrom(TransaksiAddViewModel::class.java)) {
            return TransaksiAddViewModel(transaksiRepository) as T
        } else if (modelClass.isAssignableFrom(TransaksiViewModel::class.java)) {
            return TransaksiViewModel(transaksiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}