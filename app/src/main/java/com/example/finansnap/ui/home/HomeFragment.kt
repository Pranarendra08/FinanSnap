package com.example.finansnap.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.FragmentHomeBinding
import com.example.finansnap.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

//    private lateinit var transaksiViewModel: TransaksiViewModel

    private val transaksiViewModel by viewModels<TransaksiViewModel> {
        DatabaseViewModelFactory.getInstanceData(requireActivity())
    }
//    private lateinit var repository: TransaksiRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val application = requireActivity().application
//        val factory = DatabaseViewModelFactory.getInstanceData(requireActivity())
//        val transaksiViewModel =
//            ViewModelProvider(this, factory).get(TransaksiViewModel::class.java)

//        transaksiViewModel.loadAllAvailableMonthsAndYears()

        binding.cvBulanTahun.setOnClickListener {
//            transaksiViewModel.loadAllAvailableMonthsAndYears()
            showDateSelectionDialog()
        }
//        transaksiViewModel.bulanTahunList.observe(viewLifecycleOwner, Observer { bulanTahunList ->
            // Update UI dengan bulanTahunList
//            setListTransaksiData(bulanTahunList)
//            setJumlahPengeluaran(bulanTahunList)
//            setJumlahPemasukan(bulanTahunList)
//        })

        // Contoh untuk memuat data bulan Maret 2024
//        transaksiViewModel.loadTransaksiByBulanTahun("03", "2024")

        // menampilkan daftar transaksi sesuai bulan tahun
//        transaksiViewModel.transaksiList.observe(viewLifecycleOwner, Observer { transaksiList ->
            // Update UI dengan transaksiList
//        })

        transaksiViewModel.getAllTransaksi().observe(requireActivity()) {
            setListTransaksiData(it)
            setJumlahPengeluaran(it)
            setJumlahPemasukan(it)
        }

        return root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val factory = DatabaseViewModelFactory.getInstanceData(requireActivity())
//        val transaksiViewModel =
//            ViewModelProvider(this, factory).get(TransaksiViewModel::class.java)
//        transaksiViewModel.loadAllAvailableMonthsAndYears()
//    }

    private fun showDateSelectionDialog() {
        val bulanTahunList = transaksiViewModel.bulanTahunList.value ?: return

        val items = bulanTahunList.map { "${it.bulan}-${it.tahun}" }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Bulan dan Tahun")
            .setItems(items) { dialog, which ->
                val selected = items[which]
                val (bulan, tahun) = selected.split("-")
                transaksiViewModel.loadTransaksiByBulanTahun(bulan, tahun)
            }
            .show()
    }

    private fun setJumlahPengeluaran(dataTransaksi: List<Transaksi>?) {
        var jumlah = 0
        val pengeluaranTransaksi = dataTransaksi?.filter { it.tipe == "Pengeluaran" } ?: emptyList()
        pengeluaranTransaksi?.forEach { transaksi ->
//            val tipe = transaksi.tipe
//            val tipeInt = tipe.toInt()
            jumlah += transaksi.total

            Log.d("JUMLAH Pengeluaran", "Pengeluaran : $jumlah")
            // Lakukan sesuatu dengan nilai 'tipe'
//            println(tipe)  // Contoh output ke konsol
        }
        binding.tvJumlahPengeluran.text = jumlah.toString()
    }

    private fun setJumlahPemasukan(dataTransaksi: List<Transaksi>?) {
        var jumlah = 0
        val pemasukanTransaksi = dataTransaksi?.filter { it.tipe == "Pemasukan" } ?: emptyList()
        pemasukanTransaksi?.forEach { transaksi ->
//            val tipe = transaksi.tipe
//            val tipeInt = tipe.toInt()
            jumlah += transaksi.total

            Log.d("JUMLAH Pemasukan", "Pemasukan : $jumlah")
            // Lakukan sesuatu dengan nilai 'tipe'
//            println(tipe)  // Contoh output ke konsol
        }
        binding.tvJumlahPemasukan.text = jumlah.toString()
    }

    private fun setListTransaksiData(dataTransaksi: List<Transaksi>?) {
        //set listnya harus sesuai bulan dan tahun yg ada di cv_bulan_tahun
        val adapter = TransaksiAdapter(dataTransaksi!!)
        binding.rvTransaksi.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTransaksi.setHasFixedSize(true)
        binding.rvTransaksi.adapter = adapter

        adapter.setOnItemClickCallback(object : TransaksiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Transaksi) {

                Log.i("ID TRANSAKSI ONCLICK","ID Transaksi : ${data.id}")
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