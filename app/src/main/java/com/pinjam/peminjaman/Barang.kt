package com.pinjam.peminjaman

data class Barang(
    val id: String,
    val barcode: String,
    val nama: String,
    val kategori: String,
    val deskripsi: String,
    val tersedia: Boolean,
    val dipinjamOleh: String? = null,
    val imageUrl: String = "https://via.placeholder.com/150"
)

object BarangRepository {
    val items = listOf(
        Barang("1", "BRG001", "Proyektor EPSON", "Elektronik", "Proyektor HD untuk presentasi.", true),
        Barang("2", "BRG002", "Kabel HDMI 5m", "Kabel", "Kabel HDMI high speed 5 meter.", true),
        Barang("3", "BRG003", "Pointer Logitech", "Aksesoris", "Pointer wireless untuk presentasi.", false, "Budi Santoso"),
        Barang("4", "BRG004", "Laptop ASUS", "Elektronik", "Laptop spesifikasi tinggi untuk editing.", true),
        Barang("5", "BRG005", "Speaker Portable", "Audio", "Speaker bluetooth dengan suara jernih.", false, "Budi Santoso")
    )

    val categories = listOf("Semua", "Elektronik", "Kabel", "Aksesoris", "Audio")
}

enum class RiwayatType {
    PEMINJAMAN, PENGEMBALIAN
}

data class Riwayat(
    val id: String,
    val barangNama: String,
    val tanggal: String,
    val tipe: RiwayatType,
    val status: String
)

object RiwayatRepository {
    val items = listOf(
        Riwayat("1", "Proyektor EPSON", "10 Jun 2024", RiwayatType.PEMINJAMAN, "Selesai"),
        Riwayat("2", "Laptop ASUS", "08 Jun 2024", RiwayatType.PENGEMBALIAN, "Selesai"),
        Riwayat("3", "Speaker Portable", "05 Jun 2024", RiwayatType.PEMINJAMAN, "Berlangsung"),
        Riwayat("4", "Kabel HDMI 5m", "01 Jun 2024", RiwayatType.PENGEMBALIAN, "Selesai"),
        Riwayat("5", "Pointer Logitech", "28 Mei 2024", RiwayatType.PEMINJAMAN, "Selesai")
    )
}

