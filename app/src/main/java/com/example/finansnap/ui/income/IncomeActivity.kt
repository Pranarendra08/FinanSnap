package com.example.finansnap.ui.income

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityIncomeBinding
import com.example.finansnap.ui.expense.DropdownAdapter
import com.example.finansnap.ui.expense.DropdownItem
import com.example.finansnap.ui.input.TransaksiAddViewModel
import com.example.finansnap.util.isDateValid

class IncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomeBinding


    private val transaksiAddViewModel by viewModels<TransaksiAddViewModel> {
        DatabaseViewModelFactory.getInstanceData(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.cvKategoriPemasukan.setOnClickListener{
            showDropdownDialog(this)
        }

        binding.btnSelesaiPemasukan.setOnClickListener {
            var isEmpty = false
            if (binding.etJumlahPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etJumlahPemasukan.error = "Isi total"
            }
            if (binding.etTanggalPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etTanggalPemasukan.error = "Isi tanggal"
            }
            if (!isDateValid(binding.etTanggalPemasukan.text.toString(), "dd-MM-yyyy")) {
                isEmpty = true
                binding.etTanggalPemasukan.error = "Format dd-MM-yyyy"
            }
            if (binding.tvKategoriPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.tvKategoriPemasukan.error = "Isi kategori"
            }
            if (binding.etCatatanPemasukan.text.trim().contentEquals("")) {
                isEmpty = true
                binding.etCatatanPemasukan.error = "Isi deskripsi"
            }
            if (!isEmpty) {
                val total = binding.etJumlahPemasukan.text.toString()
                val tanggal = binding.etTanggalPemasukan.text.toString()
                var kategori = binding.tvKategoriPemasukan.text.toString()
                if (kategori == "Pilih Kategori") {
                    kategori = "Lain-lain"
                }
                val deskripsi = binding.etCatatanPemasukan.text.toString()
                val transaksiEntity = Transaksi(
                    kategori = kategori,
                    tipe = "Pemasukan",
                    total = total.toInt(),
                    deskripsi = deskripsi,
                    tanggal = tanggal
                )
                transaksiAddViewModel.insertTransaksi(transaksiEntity)
                startActivity(Intent(this@IncomeActivity, MainActivity::class.java))
                finishAffinity()
            }
        }

        binding.btnBackIncome.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showDropdownDialog(context: Context) {
        val items = listOf(
            DropdownItem("Gaji"),
            DropdownItem("Bisnis"),
            DropdownItem("Penyewaan"),
            DropdownItem("Bunga"),
            DropdownItem("Deviden"),
            DropdownItem("Tunjangan"),
            DropdownItem("Penjualan"),
            DropdownItem("Bonus"),
            DropdownItem("Lain-lain")
        )

        val adapter = DropdownAdapter(context, items)

        AlertDialog.Builder(context)
            .setTitle("Pilih Kategori Pemasukanmu")
            .setAdapter(adapter) { dialog, which ->
                val selectedItem = items[which]
                binding.tvKategoriPemasukan.text = selectedItem.text
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}