package com.example.finansnap.ui.expense

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.finansnap.ViewModelFactory
import com.example.finansnap.util.createCustomTempFile
import com.example.finansnap.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val expenseViewModel by viewModels<ExpenseViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flashButton.setOnClickListener {
            toggleFlashMode()
        }

        binding.captureImage.setOnClickListener {
            takePhoto()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(1080, 1440))
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO) // Set initial flash mode
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedImageUri = output.savedUri
                    val fileImage = File(savedImageUri!!.path)
                    expenseViewModel.snapOCR(fileImage)
                    expenseViewModel.isLoading.observe(this@CameraActivity) {
                        showLoading(it)
                        if (!it) {
                            expenseViewModel.resultOcr.observe(this@CameraActivity){ ocrResult ->
                                val intent = Intent()
                                intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                                intent.putExtra(EXTRA_OCR_TANGGAL, ocrResult.tanggal)
                                intent.putExtra(EXTRA_OCR_TOTAL, ocrResult.harga.toString())
                                intent.putExtra(EXTRA_OCR_TOKO, ocrResult.deskripsi)
                                setResult(CAMERAX_RESULT, intent)
                                finish()
                            }
                        }
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, "Error gagal mengambil gambar")
                    Log.e(TAG, "onError: ${exc.message}")
                    finish()
                }
            }
        )
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun toggleFlashMode() {
        val newMode = when (imageCapture!!.flashMode) {
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_OFF
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_AUTO
            ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_ON
            else -> ImageCapture.FLASH_MODE_AUTO
        }

        imageCapture!!.flashMode = newMode
        binding.flashButton.text = when (newMode) {
            ImageCapture.FLASH_MODE_ON -> "Flash: ON"
            ImageCapture.FLASH_MODE_OFF -> "Flash: OFF"
            ImageCapture.FLASH_MODE_AUTO -> "Flash: AUTO"
            else -> "Flash: AUTO"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvProcessing.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.viewFinder.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.captureImage.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.flashButton.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.borderView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val EXTRA_OCR_TANGGAL = "dd/mm/yyyy"
        const val EXTRA_OCR_TOKO = "Toko"
        const val EXTRA_OCR_TOTAL = "0"
        const val CAMERAX_RESULT = 200
    }
}