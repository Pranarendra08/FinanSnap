package com.example.finansnap.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finansnap.data.BulanTahun
import java.util.Date

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(transaksi: Transaksi)

    @Delete
    fun delete(transaksi: Transaksi)

    @Query("SELECT * from transaksi ORDER BY id ASC")
    fun getAllTransaksi(): LiveData<List<Transaksi>>

    @Query("SELECT * FROM transaksi WHERE tanggal = :tanggal")
    fun getTransaksiBulanan(tanggal: String): LiveData<List<Transaksi>>

    @Query("SELECT DISTINCT substr(tanggal, 4, 2) AS bulan, substr(tanggal, 7, 4) AS tahun FROM Transaksi")
    fun getAllAvailableMonthsAndYears(): List<BulanTahun>

    @Query("SELECT * FROM Transaksi WHERE substr(tanggal, 4, 2) = :bulan AND substr(tanggal, 7, 4) = :tahun")
    fun getTransaksiByBulanTahun(bulan: String, tahun: String): List<Transaksi>

    @Query("SELECT * FROM transaksi WHERE id = :transaksiId")
    fun getTransaksiById(transaksiId: Int): LiveData<Transaksi>
}