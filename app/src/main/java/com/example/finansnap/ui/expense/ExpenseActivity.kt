package com.example.finansnap.ui.expense

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.ViewModelFactory
import com.example.finansnap.api.OCRResult
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityExpenseBinding
import com.example.finansnap.ui.expense.CameraActivity.Companion.CAMERAX_RESULT
import com.example.finansnap.ui.home.TransaksiAddDeleteViewModel
import com.example.finansnap.ui.home.TransaksiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val expenseViewModel by viewModels<ExpenseViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val transaksiAddDeleteViewModel by viewModels<TransaksiAddDeleteViewModel> {
        DatabaseViewModelFactory.getInstanceData(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        binding.btnSnap.setOnClickListener {
            startCameraX()
            expenseViewModel.resultOcr.observe(this){
                Log.d("resultOcr di btnSnap ", "${it.toko} ${it.harga} ${it.tanggal}")
                setInputText(it)
            }
        }

        expenseViewModel.resultOcr.observe(this){
            Log.d("resultOcr di expense", "${it.toko} ${it.harga} ${it.tanggal}")
            setInputText(it)
        }

        expenseViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.cvKategoriExpense.setOnClickListener{
            showDropdownDialog(this)
        }

        // save data edittext ke database
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        binding.btnSelesaiPengeluaran.setOnClickListener {
            var isEmpty = false
            if (binding.editTextNumber.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextNumber.error = "Isi total"//getString(R.string.isi_berat)
            }
            if (binding.editTextDate.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextDate.error = "Isi tanggal"//getString(R.string.isi_tinggi)
            }
            if (binding.tvKategoriExpense.text.trim().contentEquals("")) {
                isEmpty = true
                binding.tvKategoriExpense.error = "Isi kategori"//getString(R.string.isi_berat)
            }
            if (binding.editTextText.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextText.error = "Isi deskripsi"//getString(R.string.isi_tinggi)
            }
            if (!isEmpty) {
                val total = binding.editTextNumber.text.toString()
                val tanggal = binding.editTextDate.text.toString()
                val kategori = binding.tvKategoriExpense.text.toString()
                val deskripsi = binding.editTextText.text.toString()
                val transaksiEntity = Transaksi(
                    kategori = kategori,
                    tipe = "Pengeluaran",
                    total = total.toInt(),
                    deskripsi = deskripsi,
                    tanggal = tanggal
                )
                transaksiAddDeleteViewModel.insertTransaksi(transaksiEntity)
                startActivity(Intent(this@ExpenseActivity, MainActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun setInputText(ocrResult: OCRResult?) {
//        binding.editTextText.setText(it.toko)
//        binding.editTextDate.setText(it.tanggal)
//        binding.editTextNumber.setText(it.harga)
        Log.d("setInputText", "fungsi setInputText terpanggil")

        ocrResult?.toko?.let {binding.editTextText.setText(it) }
        ocrResult?.tanggal?.let { binding.editTextDate.setText(it) }
        ocrResult?.harga?.let { binding.editTextNumber.setText(it) }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            imageOCR()

            binding.editTextText.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TOKO))
            binding.editTextDate.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TANGGAL))
            binding.editTextNumber.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TOTAL))
            expenseViewModel.resultOcr.observe(this){ ocrResult ->
                Log.d("resultOcr di launcherIntentCameraX", "${ocrResult.toko} ${ocrResult.harga} ${ocrResult.tanggal}")
                setInputText(ocrResult)
            }
        }
    }

    private fun imageOCR() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
//            var hasilOcr = expenseViewModel.resultOcr//.observe(this){ ocrResult ->
//                Log.d("resultOcr di imageOCR", "${ocrResult.toko} ${ocrResult.harga} ${ocrResult.tanggal}")
//                setInputText(ocrResult)
//            binding.editTextText.setText(hasilOcr.value!!.toko)
//            binding.editTextDate.setText(hasilOcr.value!!.tanggal)
//            binding.editTextNumber.setText(hasilOcr.value!!.harga)

        }
    }

    fun showDropdownDialog(context: Context) {
        val items = listOf(
            DropdownItem("Transportasi"),
            DropdownItem("Makanan"),
            DropdownItem("Belanja"),
            DropdownItem("Pakaian"),
            DropdownItem("Kesehatan"),
            DropdownItem("Listrik"),
            DropdownItem("Internet"),
            DropdownItem("Pajak"),
            DropdownItem("Hiburan"),
            DropdownItem("Lain-lain")
        )

        val adapter = DropdownAdapter(context, items)

        AlertDialog.Builder(context)
            .setTitle("Select an Item")
            .setAdapter(adapter) { dialog, which ->
                val selectedItem = items[which]
                binding.tvKategoriExpense.text = selectedItem.text
//                Toast.makeText(context, "Selected: ${selectedItem.text}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}