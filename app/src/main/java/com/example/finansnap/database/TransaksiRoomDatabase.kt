package com.example.finansnap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaksi::class], version = 1)
abstract class TransaksiRoomDatabase : RoomDatabase() {
    abstract fun transaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var INSTANCE: TransaksiRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): TransaksiRoomDatabase {
            if (INSTANCE == null) {
                synchronized(TransaksiRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        TransaksiRoomDatabase::class.java, "transaksi_database")
                        .build()
                }
            }
            return INSTANCE as TransaksiRoomDatabase
        }
    }
}