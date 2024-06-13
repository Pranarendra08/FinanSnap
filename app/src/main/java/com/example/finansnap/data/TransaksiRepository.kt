package com.example.finansnap.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.finansnap.database.Transaksi
import com.example.finansnap.database.TransaksiDao
import com.example.finansnap.database.TransaksiRoomDatabase
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TransaksiRepository(private val transaksiDao: TransaksiDao) { //application: Application
//    private val mTransaksiDao: TransaksiDao
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

//    init {
//        val db = TransaksiRoomDatabase.getDatabase(application)
//        mTransaksiDao = db.transaksiDao()
//    }

    fun getAllTransaksi(): LiveData<List<Transaksi>> = transaksiDao.getAllTransaksi()

//    fun getTransaksiBulanan(tanggal: String): LiveData<List<Transaksi>> = transaksiDao.getTransaksiBulanan(tanggal)

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
            callback(result)
        }
    }

//    fun getTransaksiByBulanTahun(bulan: String, tahun: String): List<Transaksi> {
//        return transaksiDao.getTransaksiByBulanTahun(bulan, tahun)
//    }

    fun getTransaksiByBulanTahun(bulan: String, tahun: String, callback: (List<Transaksi>) -> Unit) {
        executorService.execute {
            val result = transaksiDao.getTransaksiByBulanTahun(bulan, tahun)
            callback(result)
        }
    }
}