package com.example.mydatasiswa.modeldata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataSiswa(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("nama")
    val nama: String,

    @SerialName("alamat")
    val alamat: String,

    @SerialName("telpon")
    val telpon: String
)