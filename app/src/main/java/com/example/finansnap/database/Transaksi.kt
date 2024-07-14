package com.example.finansnap.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Transaksi")
@Parcelize
data class Transaksi (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "tipe")
    val tipe: String,

    @ColumnInfo(name = "kategori")
    var kategori: String,

    @ColumnInfo(name = "total")
    var total: Int,

    @ColumnInfo(name = "deskripsi")
    var deskripsi: String,

    @ColumnInfo(name = "tanggal")
    var tanggal: String

) : Parcelable