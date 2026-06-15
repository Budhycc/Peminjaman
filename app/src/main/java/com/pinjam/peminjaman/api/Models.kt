package com.pinjam.peminjaman.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    @SerialName("id_aset") val idAset: Int? = null,
    @SerialName("kode_aset") val kodeAset: String,
    @SerialName("nama_aset") val namaAset: String,
    val kategori: String,
    val merk: String? = null,
    val lokasi: String? = null,
    val kondisi: String,
    val status: String,
    @SerialName("qr_code") val qrCode: String? = null
)

@Serializable
data class LoanRequest(
    @SerialName("id_aset") val idAset: Int,
    @SerialName("rencana_kembali") val rencanaKembali: String,
    val catatan: String? = null
)

@Serializable
data class LoanResponse(
    @SerialName("id_peminjaman") val idPeminjaman: Int? = null,
    @SerialName("id_user") val idUser: Int? = null,
    @SerialName("id_aset") val idAset: Int? = null,
    @SerialName("tanggal_pinjam") val tanggalPinjam: String,
    @SerialName("rencana_kembali") val rencanaKembali: String,
    val status: String,
    @SerialName("aset") val asset: AssetResponse? = null
)

@Serializable
data class ReturnRequest(
    @SerialName("id_peminjaman") val idPeminjaman: Int,
    @SerialName("kondisi_kembali") val kondisiKembali: String,
    val catatan: String? = null
)
