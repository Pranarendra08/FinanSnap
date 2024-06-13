package com.example.finansnap.ui.income

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.R
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityExpenseBinding
import com.example.finansnap.databinding.ActivityIncomeBinding
import com.example.finansnap.ui.expense.DropdownAdapter
import com.example.finansnap.ui.expense.DropdownItem
import com.example.finansnap.ui.home.TransaksiAddDeleteViewModel

class IncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomeBinding


    private val transaksiAddDeleteViewModel by viewModels<TransaksiAddDeleteViewModel> {
        DatabaseViewModelFactory.getInstanceData(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvKategoriPemasukan.setOnClickListener{
            showDropdownDialog(this)
        }

        binding.btnSelesaiPemasukan.setOnClickListener {
            var isEmpty = false
            if (binding.etJumlahPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etJumlahPemasukan.error = "Isi total"//getString(R.string.isi_berat)
            }
            if (binding.etTanggalPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etTanggalPemasukan.error = "Isi tanggal"//getString(R.string.isi_tinggi)
            }
            if (binding.tvKategoriPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.tvKategoriPemasukan.error = "Isi kategori"//getString(R.string.isi_berat)
            }
            if (binding.etCatatanPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etCatatanPemasukan.error = "Isi deskripsi"//getString(R.string.isi_tinggi)
            }
            if (!isEmpty) {
                val total = binding.etJumlahPemasukan.text.toString()
                val tanggal = binding.etTanggalPemasukan.text.toString()
                val kategori = binding.tvKategoriPemasukan.text.toString()
                val deskripsi = binding.etCatatanPemasukan.text.toString()
                val transaksiEntity = Transaksi(
                    kategori = kategori,
                    tipe = "Pemasukan",
                    total = total.toInt(),
                    deskripsi = deskripsi,
                    tanggal = tanggal
                )
                transaksiAddDeleteViewModel.insertTransaksi(transaksiEntity)
                startActivity(Intent(this@IncomeActivity, MainActivity::class.java))
                finishAffinity()
            }
        }
    }

    fun showDropdownDialog(context: Context) {
        val items = listOf(
            DropdownItem("Bisnis"),
            DropdownItem("Penyewaan"),
            DropdownItem("gaji"),
            DropdownItem("Bunga"),
            DropdownItem("Deviden"),
            DropdownItem("Tunjangan"),
            DropdownItem("Penjualan"),
            DropdownItem("Bonus"),
            DropdownItem("Lain-lain")
        )

        val adapter = DropdownAdapter(context, items)

        AlertDialog.Builder(context)
            .setTitle("Select an Item")
            .setAdapter(adapter) { dialog, which ->
                val selectedItem = items[which]
                binding.tvKategoriPemasukan.text = selectedItem.text
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}