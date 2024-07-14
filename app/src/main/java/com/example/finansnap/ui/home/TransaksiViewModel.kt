package com.example.finansnap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finansnap.data.BulanTahun
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.database.Transaksi
import com.example.finansnap.util.toDate

class TransaksiViewModel(private val transaksiRepository: TransaksiRepository) : ViewModel() {
    private val _transaksiList = MutableLiveData<List<Transaksi>>()
    val transaksiList: LiveData<List<Transaksi>> = _transaksiList

    private val _bulanTahunList = MutableLiveData<List<BulanTahun>>()
    val bulanTahunList: LiveData<List<BulanTahun>> = _bulanTahunList

    private val _sortedTransaksi = MutableLiveData<List<Transaksi>>()
    val sortedTransaksi: LiveData<List<Transaksi>> get() = _sortedTransaksi

    init {
        // Observe data from repository
        transaksiRepository.sortedTransaksi.observeForever { list ->
            _sortedTransaksi.value = list.sortedByDescending {
                it.tanggal.toDate()
            }
        }
    }

    fun getAllTransaksi(): LiveData<List<Transaksi>> = transaksiRepository.getAllTransaksi()

    fun loadAllAvailableMonthsAndYears() {
        transaksiRepository.getAllAvailableMonthsAndYears { result ->
            _bulanTahunList.postValue(result)
        }
    }

    fun loadTransaksiByBulanTahun(bulan: String, tahun: String) {
        transaksiRepository.getTransaksiByBulanTahun(bulan, tahun) { result ->
            _transaksiList.postValue(result)
        }
    }


}