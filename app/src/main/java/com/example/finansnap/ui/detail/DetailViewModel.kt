package com.example.finansnap.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.finansnap.data.TransaksiRepository
import com.example.finansnap.database.Transaksi
import kotlinx.coroutines.launch

class DetailViewModel(private val transaksiRepository: TransaksiRepository): ViewModel() {

    private val _transaksiId = MutableLiveData<Int>()

    private val _transaksi = _transaksiId.switchMap { id ->
        transaksiRepository.getTransaksiById(id)
    }

    val transaksi: LiveData<Transaksi> = _transaksi

    fun setTransaksiId(transaksiId: Int?) {
        if (transaksiId == _transaksiId.value){
            return
        }
        _transaksiId.value = transaksiId!!
    }

    fun deleteTransaksi() {
        viewModelScope.launch {
            _transaksi.value?.let { transaksiRepository.deleteTransaksi(it) }
        }
    }
}