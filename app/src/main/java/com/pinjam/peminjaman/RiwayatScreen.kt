package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme

@Composable
fun RiwayatScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Semua", "Pinjam", "Kembali")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        val filteredRiwayat = when (selectedTabIndex) {
            1 -> RiwayatRepository.items.filter { it.tipe == RiwayatType.PEMINJAMAN }
            2 -> RiwayatRepository.items.filter { it.tipe == RiwayatType.PENGEMBALIAN }
            else -> RiwayatRepository.items
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredRiwayat) { item ->
                RiwayatItem(riwayat = item)
            }
        }
    }
}

@Composable
fun RiwayatItem(riwayat: Riwayat) {
    val icon: ImageVector
    val iconColor: Color
    val typeText: String
    
    // Find associated barang to get image and category (optional, based on mock data)
    val associatedBarang = BarangRepository.items.find { it.nama == riwayat.barangNama }

    if (riwayat.tipe == RiwayatType.PEMINJAMAN) {
        icon = Icons.Default.Outbox
        iconColor = MaterialTheme.colorScheme.primary
        typeText = "Peminjaman"
    } else {
        icon = Icons.Default.AssignmentReturn
        iconColor = Color(0xFF4CAF50) // Green
        typeText = "Pengembalian"
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(riwayat.barangNama, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${associatedBarang?.kategori ?: "Barang"} • $typeText",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = riwayat.tanggal,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = iconColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Badge(
                    containerColor = if (riwayat.status == "Berlangsung") 
                        MaterialTheme.colorScheme.tertiaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(riwayat.status)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatScreenPreview() {
    PeminjamanTheme {
        RiwayatScreen()
    }
}
