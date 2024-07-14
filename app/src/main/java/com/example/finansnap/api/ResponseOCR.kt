package com.example.finansnap.api

import com.google.gson.annotations.SerializedName

data class ResponseOCR(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("OCR_result")
	val ocrResult: OCRResult
)

data class OCRResult(

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("tanggal")
	val tanggal: String
)
