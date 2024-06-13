package com.example.finansnap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finansnap.data.BulanTahun
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.database.Transaksi
import kotlinx.coroutines.launch

class TransaksiViewModel(private val transaksiRepository: TransaksiRepository) : ViewModel() {
    private val _transaksiList = MutableLiveData<List<Transaksi>>()
    val transaksiList: LiveData<List<Transaksi>> = _transaksiList

    private val _bulanTahunList = MutableLiveData<List<BulanTahun>>()
    val bulanTahunList: LiveData<List<BulanTahun>> = _bulanTahunList

    fun getAllTransaksi(): LiveData<List<Transaksi>> = transaksiRepository.getAllTransaksi()

    fun loadTransaksiByBulanTahun(bulan: String, tahun: String) {
        viewModelScope.launch {
            val transaksi = transaksiRepository.getTransaksiByBulanTahun(bulan, tahun)
            _transaksiList.postValue(transaksi)
        }
    }

    fun loadAllAvailableMonthsAndYears() {
        viewModelScope.launch {
            val bulanTahun = transaksiRepository.getAllAvailableMonthsAndYears()
            _bulanTahunList.postValue(bulanTahun)
        }
    }
}