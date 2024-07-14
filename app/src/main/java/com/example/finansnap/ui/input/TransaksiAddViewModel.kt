package com.example.finansnap.ui.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.database.Transaksi
import kotlinx.coroutines.launch

class TransaksiAddViewModel(private val transaksiRepository: TransaksiRepository) : ViewModel() { //application: Application

    fun insertTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            transaksiRepository.insertTransaksi(transaksi)
        }
    }

}