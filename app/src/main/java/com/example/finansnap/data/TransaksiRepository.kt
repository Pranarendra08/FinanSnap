package com.example.finansnap.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.finansnap.database.Transaksi
import com.example.finansnap.database.TransaksiDao
import com.example.finansnap.database.TransaksiRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TransaksiRepository(private val transaksiDao: TransaksiDao) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    companion object {
        @Volatile
        private var instance: TransaksiRepository? = null

        fun getInstance(context: Context): TransaksiRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TransaksiRoomDatabase.getDatabase(context)
                    instance = TransaksiRepository(database.transaksiDao())
                }
                return instance as TransaksiRepository
            }

        }
    }

    fun getAllTransaksi(): LiveData<List<Transaksi>> = transaksiDao.getAllTransaksi()

    val sortedTransaksi: LiveData<List<Transaksi>> = transaksiDao.getTransaksiSortedByDate()

    fun insertTransaksi(transaksi: Transaksi) {
        executorService.execute {
            transaksiDao.insert(transaksi)
        }
    }

    fun deleteTransaksi(transaksi: Transaksi) {
        executorService.execute {
            transaksiDao.delete(transaksi)
        }
    }

    fun getTransaksiById(transaksiId: Int): LiveData<Transaksi> {
        return transaksiDao.getTransaksiById(transaksiId)
    }

    fun getAllAvailableMonthsAndYears(callback: (List<BulanTahun>) -> Unit) {
        executorService.execute {
            val result = transaksiDao.getAllAvailableMonthsAndYears()
            val sortedResult = result.sortedWith(compareByDescending<BulanTahun> { it.tahun.toInt() }
                .thenByDescending { it.bulan.toInt() })
            callback(sortedResult)
        }
    }

    fun getTransaksiByBulanTahun(bulan: String, tahun: String, callback: (List<Transaksi>) -> Unit) {
        executorService.execute {
            val result = transaksiDao.getTransaksiByBulanTahun(bulan, tahun)
            val sortedResult = result.sortedByDescending { it.tanggal }
            callback(sortedResult)
        }
    }

}