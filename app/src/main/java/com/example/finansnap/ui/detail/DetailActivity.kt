package com.example.finansnap.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.R
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityDetailBinding
import com.example.finansnap.util.withCurrencyFormat
import com.example.finansnap.util.withDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel> {
        DatabaseViewModelFactory.getInstanceData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val id = intent.getIntExtra(ID, 1)

        detailViewModel.setTransaksiId(id)
        detailViewModel.transaksi.observe(this, Observer(this::detail))

        binding.btnBackDetail.setOnClickListener {
            onBackPressed()
        }

        binding.btnDeleteDetail.setOnClickListener {
            detailViewModel.deleteTransaksi()
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun detail(transaksi: Transaksi) {
        if (transaksi != null) {
            binding.tvTotalDetail.text = transaksi.total.toString().withCurrencyFormat()
            binding.tvTanggalDetail.text = transaksi.tanggal.withDateFormat()
            binding.tvKategoriDetail.text = transaksi.kategori
            binding.tvCatatanDetail.text = transaksi.deskripsi

            if (transaksi.tipe == "Pengeluaran") {
                binding.viewTipe.background = ContextCompat.getDrawable(this, R.drawable.shape_box_expense)
                binding.tvTipeTransaksi.text = "Pengeluaran"
            }
        }
    }

    companion object {
        const val ID = "ID"
    }
}