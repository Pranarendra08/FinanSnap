package com.example.finansnap.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.FragmentHomeBinding
import com.example.finansnap.ui.detail.DetailActivity
import com.example.finansnap.util.withCurrencyFormat
import com.example.finansnap.util.withFilterDateFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val adapter = TransaksiAdapter(listOf())

    private val transaksiViewModel by viewModels<TransaksiViewModel> {
        DatabaseViewModelFactory.getInstanceData(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        transaksiViewModel.loadAllAvailableMonthsAndYears()

        binding.cvBulanTahun.setOnClickListener {
            showDateSelectionDialog()
        }

        transaksiViewModel.transaksiList.observe(viewLifecycleOwner) { transaksiList ->
            setListTransaksiData(transaksiList)
            setJumlahPengeluaran(transaksiList)
            setJumlahPemasukan(transaksiList)
        }

        transaksiViewModel.getAllTransaksi().observe(requireActivity()) {
            setListTransaksi()
            setJumlahPengeluaran(it)
            setJumlahPemasukan(it)
        }

        return root
    }

    private fun setListTransaksi() {
        binding.rvTransaksi.adapter = adapter
        binding.rvTransaksi.layoutManager = LinearLayoutManager(requireActivity())
        transaksiViewModel.sortedTransaksi.observe(viewLifecycleOwner) { transaksiList ->
            transaksiList?.let {
                adapter.updateData(it)
            }
        }
        adapter.setOnItemClickCallback(object : TransaksiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Transaksi) {
                startActivity(
                    Intent(requireActivity(), DetailActivity::class.java)
                        .putExtra(DetailActivity.ID, data.id)
                )
            }
        })
    }

    private fun showDateSelectionDialog() {
        val bulanTahunList = transaksiViewModel.bulanTahunList.value ?: return

        val items = bulanTahunList.map { "${it.bulan}-${it.tahun}".withFilterDateFormat() }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Bulan dan Tahun")
            .setItems(items) { _, which ->
                val selected = items[which]
                val (bulan, tahun) = bulanTahunList[which].let { it.bulan to it.tahun }//selected.split("-")
                binding.tvBulanTahun.text = selected
                transaksiViewModel.loadTransaksiByBulanTahun(bulan, tahun)
            }
            .show()
    }

    private fun setJumlahPengeluaran(dataTransaksi: List<Transaksi>?) {
        var jumlah = 0
        val pengeluaranTransaksi = dataTransaksi?.filter { it.tipe == "Pengeluaran" } ?: emptyList()
        pengeluaranTransaksi.forEach { transaksi ->
            jumlah += transaksi.total
        }
        binding.tvJumlahPengeluran.text = jumlah.toString().withCurrencyFormat()
    }

    private fun setJumlahPemasukan(dataTransaksi: List<Transaksi>?) {
        var jumlah = 0
        val pemasukanTransaksi = dataTransaksi?.filter { it.tipe == "Pemasukan" } ?: emptyList()
        pemasukanTransaksi.forEach { transaksi ->
            jumlah += transaksi.total

        }
        binding.tvJumlahPemasukan.text = jumlah.toString().withCurrencyFormat()
    }

    private fun setListTransaksiData(dataTransaksi: List<Transaksi>?) {
        val adapter = TransaksiAdapter(dataTransaksi!!)
        binding.rvTransaksi.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTransaksi.setHasFixedSize(true)
        binding.rvTransaksi.adapter = adapter

        adapter.setOnItemClickCallback(object : TransaksiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Transaksi) {
                startActivity(
                    Intent(requireActivity(), DetailActivity::class.java)
                        .putExtra(DetailActivity.ID, data.id)
                )
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}