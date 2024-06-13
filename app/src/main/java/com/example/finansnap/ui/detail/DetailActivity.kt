package com.example.finansnap.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.R
import com.example.finansnap.ViewModelFactory
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityDetailBinding
import com.example.finansnap.databinding.ActivityExpenseBinding
import com.example.finansnap.ui.home.TransaksiAddDeleteViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

//    private lateinit var viewModel: DetailViewModel

    private val transaksiAddDeleteViewModel by viewModels<TransaksiAddDeleteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val detailViewModel by viewModels<DetailViewModel> {
        DatabaseViewModelFactory.getInstanceData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(ID, 1)//intent.extras?.get(ID) as Int

        Log.i("ID DETAIL TRANSAKSI","ID Transaksi : $id")

//        val viewModelFactory = DatabaseViewModelFactory.getInstanceData(this)
//        viewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]

//        viewModel.setTransaksiId(id)
//        viewModel.transaksi.observe(this, Observer(this::detail))
        detailViewModel.setTransaksiId(id)
        detailViewModel.transaksi.observe(this, Observer(this::detail))



        binding.btnHapusDetail.setOnClickListener {
            //panggil query detele
            detailViewModel.deleteTransaksi()
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun detail(transaksi: Transaksi) {
        if (transaksi != null) {
            binding.tvTotalDetail.text = transaksi.total.toString()
            binding.tvTanggalDetail.text = transaksi.tanggal
            binding.tvKategoriDetail.text = transaksi.kategori
            binding.tvCatatanDetail.text = transaksi.deskripsi
        }
    }

    companion object {
        const val ID = "ID"
    }
}