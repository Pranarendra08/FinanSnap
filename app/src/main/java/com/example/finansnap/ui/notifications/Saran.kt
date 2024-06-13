package com.example.finansnap.ui.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Saran(
    val judul: String,
    val link: String
) : Parcelable
