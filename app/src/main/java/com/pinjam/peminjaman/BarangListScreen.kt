package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.api.AssetResponse
import com.pinjam.peminjaman.api.RetrofitClient
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangListScreen(onBack: () -> Unit, onItemClick: (Barang) -> Unit, onScanClick: () -> Unit) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    
    var assets by remember { mutableStateOf<List<AssetResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = remember(assets) {
        listOf("Semua") + assets.map { it.kategori }.distinct().sorted()
    }

    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        if (token != null) {
            try {
                val response = RetrofitClient.instance.getAssets("Bearer $token")
                if (response.isSuccessful) {
                    assets = response.body() ?: emptyList()
                } else {
                    errorMessage = "Gagal memuat aset: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    val filteredItems = assets.filter {
        (selectedCategory == "Semua" || it.kategori == selectedCategory) &&
                it.namaAset.contains(searchQuery, ignoreCase = true)
    }.map { asset ->
        // Map API response to local Barang model
        Barang(
            id = (asset.idAset ?: 0).toString(),
            barcode = asset.kodeAset,
            nama = asset.namaAset,
            kategori = asset.kategori,
            deskripsi = "${asset.merk ?: ""} ${asset.lokasi ?: ""}".trim(),
            tersedia = asset.status == "tersedia"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Barang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onScanClick) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            } else {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Cari barang...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )

                // Filter Kategori
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(categories) { kategori ->
                        FilterChip(
                            selected = selectedCategory == kategori,
                            onClick = { selectedCategory = kategori },
                            label = { Text(kategori) }
                        )
                    }
                }

                // List Barang
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems) { barang ->
                        BarangItem(barang = barang, onClick = { onItemClick(barang) })
                    }
                }
            }
        }
    }
}

@Composable
fun BarangItem(barang: Barang, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(barang.nama, style = MaterialTheme.typography.titleMedium)
                Text(barang.kategori, style = MaterialTheme.typography.bodySmall)
            }
            Badge(
                containerColor = if (barang.tersedia) Color(0xFF4CAF50) else Color(0xFFF44336),
                contentColor = Color.White
            ) {
                Text(if (barang.tersedia) "Tersedia" else "Dipinjam")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarangListScreenPreview() {
    PeminjamanTheme {
        BarangListScreen(onBack = {}, onItemClick = {}, onScanClick = {})
    }
}
