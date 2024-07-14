package com.example.finansnap.ui.expense

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.finansnap.DatabaseViewModelFactory
import com.example.finansnap.MainActivity
import com.example.finansnap.ViewModelFactory
import com.example.finansnap.api.OCRResult
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ActivityExpenseBinding
import com.example.finansnap.ui.expense.CameraActivity.Companion.CAMERAX_RESULT
import com.example.finansnap.ui.input.TransaksiAddViewModel
import com.example.finansnap.util.isDateValid

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding
//    private var currentImageUri: Uri? = null

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

    private val transaksiAddViewModel by viewModels<TransaksiAddViewModel> {
        DatabaseViewModelFactory.getInstanceData(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        binding.btnSnap.setOnClickListener {
            startCameraX()
            expenseViewModel.resultOcr.observe(this){
                setInputText(it)
            }
        }

        binding.cvKategoriExpense.setOnClickListener{
            showDropdownDialog(this)
        }

        binding.btnSelesaiPengeluaran.setOnClickListener {
            var isEmpty = false
            if (binding.editTextNumber.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextNumber.error = "Isi total"
            }
            if (binding.editTextDate.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextDate.error = "Isi tanggal"
            }
            if (!isDateValid(binding.editTextDate.text.toString(), "dd-MM-yyyy")) {
                isEmpty = true
                binding.editTextDate.error = "Format dd-MM-yyyy"
            }
            if (binding.tvKategoriExpense.text.trim().contentEquals("")) {
                isEmpty = true
                binding.tvKategoriExpense.error = "Isi kategori"
            }
            if (binding.editTextText.text.trim().contentEquals("")) {
                isEmpty = true
                binding.editTextText.error = "Isi deskripsi"
            }
            if (!isEmpty) {
                val total = binding.editTextNumber.text.toString()
                val tanggal = binding.editTextDate.text.toString()
                var kategori = binding.tvKategoriExpense.text.toString()
                if (kategori == "Pilih Kategori") {
                    kategori = "Lain-lain"
                }
                val deskripsi = binding.editTextText.text.toString()
                val transaksiEntity = Transaksi(
                    kategori = kategori,
                    tipe = "Pengeluaran",
                    total = total.toInt(),
                    deskripsi = deskripsi,
                    tanggal = tanggal
                )
                transaksiAddViewModel.insertTransaksi(transaksiEntity)
                startActivity(Intent(this@ExpenseActivity, MainActivity::class.java))
                finishAffinity()
            }
        }

        binding.btnBackExpense.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setInputText(ocrResult: OCRResult?) {
        ocrResult?.deskripsi?.let {binding.editTextText.setText(it) }
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
//            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()

            binding.editTextText.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TOKO))
            binding.editTextDate.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TANGGAL))
            binding.editTextNumber.setText(it.data?.getStringExtra(CameraActivity.EXTRA_OCR_TOTAL))
            expenseViewModel.resultOcr.observe(this){ ocrResult ->
                setInputText(ocrResult)
            }
        }
    }

    private fun showDropdownDialog(context: Context) {
        val items = listOf(
            DropdownItem("Kebutuhan Primer"),
            DropdownItem("Pakaian"),
            DropdownItem("Makanan"),
            DropdownItem("Rumah"),
            DropdownItem("Kesehatan"),
            DropdownItem("Pendidikan"),
            DropdownItem("Kebutuhan Sekunder"),
            DropdownItem("Belanja"),
            DropdownItem("Transportasi"),
            DropdownItem("Listrik"),
            DropdownItem("Internet"),
            DropdownItem("Hiburan"),
            DropdownItem("Kebutuhan Tersier"),
            DropdownItem("Perhiasan"),
            DropdownItem("Kendaraan"),
            DropdownItem("Wisata"),
            DropdownItem("Pajak"),
            DropdownItem("Hutang"),
            DropdownItem("Lain-lain")
        )

        val adapter = DropdownAdapter(context, items)

        AlertDialog.Builder(context)
            .setTitle("Pilih Kategori Pengeluaranmu")
            .setAdapter(adapter) { dialog, which ->
                val selectedItem = items[which]
                binding.tvKategoriExpense.text = selectedItem.text
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}