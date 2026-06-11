package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangDetailScreen(barang: Barang, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deskripsi Barang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Placeholder Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Foto Barang",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = barang.nama,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            AssistChip(
                onClick = {},
                label = { Text(barang.kategori) },
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (barang.tersedia) "Tersedia untuk dipinjam" else "Sedang dalam peminjaman",
                color = if (barang.tersedia) Color(0xFF4CAF50) else Color(0xFFF44336),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Deskripsi",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = barang.deskripsi,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Action Pinjam */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = barang.tersedia
            ) {
                Text(if (barang.tersedia) "Ajukan Peminjaman" else "Barang Tidak Tersedia")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarangDetailScreenPreview() {
    PeminjamanTheme {
        BarangDetailScreen(
            barang = BarangRepository.items[0],
            onBack = {}
        )
    }
}
