package com.example.mydatasiswa.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydatasiswa.modeldata.DataSiswa
import com.example.mydatasiswa.repositori.RepositoryDataSiswa
import kotlinx.coroutines.launch
import retrofit2.Response

class EntryViewModel(
    private val repositoryDataSiswa: RepositoryDataSiswa
) : ViewModel() {

    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private fun validasiInput(
        uiState: DetailSiswa = uiStateSiswa.detailSiswa
    ): Boolean {
        return with(uiState) {
            nama.isNotBlank() &&
                    alamat.isNotBlank() &&
                    telpon.isNotBlank()
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa = UIStateSiswa(
            detailSiswa = detailSiswa,
            isEntryValid = detailSiswa.nama.isNotBlank() &&
                    detailSiswa.alamat.isNotBlank() &&
                    detailSiswa.telpon.isNotBlank()
        )
    }

    fun addSiswa(onResult: (Boolean) -> Unit) {
        if (!validasiInput()) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            val berhasil = try {
                val response = repositoryDataSiswa.postDataSiswa(
                    uiStateSiswa.detailSiswa.toDataSiswa()
                )

                if (response.isSuccessful) {
                    Log.d("ENTRY_SISWA", "Data berhasil disimpan")
                    true
                } else {
                    Log.e(
                        "ENTRY_SISWA",
                        "Gagal: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                    false
                }
            } catch (e: Exception) {
                Log.e("ENTRY_SISWA", "Terjadi kesalahan", e)
                false
            }

            onResult(berhasil)
        }
    }
}

data class UIStateSiswa(
    val detailSiswa: DetailSiswa = DetailSiswa(),
    val isEntryValid: Boolean = false
)

data class DetailSiswa(
    val id: Int = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

fun DetailSiswa.toDataSiswa(): DataSiswa = DataSiswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)

fun DataSiswa.toDetailSiswa(): DetailSiswa = DetailSiswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)

fun DataSiswa.toUiStateSiswa(
    isEntryValid: Boolean = false
): UIStateSiswa = UIStateSiswa(
    detailSiswa = toDetailSiswa(),
    isEntryValid = isEntryValid
)