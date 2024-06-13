package com.example.finansnap.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.database.Transaksi
import kotlinx.coroutines.launch
import java.util.Date

class TransaksiAddDeleteViewModel(private val transaksiRepository: TransaksiRepository) : ViewModel() { //application: Application

    fun insertTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            transaksiRepository.insertTransaksi(transaksi)
        }
    }

}