package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengembalianScreen(onBack: () -> Unit) {
    // Mocking borrowed items by "Budi Santoso"
    val borrowedItems = BarangRepository.items.filter { it.dipinjamOleh == "Budi Santoso" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengembalian Barang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
            if (borrowedItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Anda tidak memiliki tanggungan peminjaman.")
                }
            } else {
                Text(
                    text = "Pilih barang yang ingin dikembalikan:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(borrowedItems) { barang ->
                        BorrowedItemRow(barang = barang)
                    }
                }
            }
        }
    }
}

@Composable
fun BorrowedItemRow(barang: Barang) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Placeholder
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IMG", style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(barang.nama, style = MaterialTheme.typography.titleMedium)
                    Text(barang.kategori, style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { /* Action Kembalikan */ }) {
                    Text("Kembalikan")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PengembalianScreenPreview() {
    PeminjamanTheme {
        PengembalianScreen(onBack = {})
    }
}
