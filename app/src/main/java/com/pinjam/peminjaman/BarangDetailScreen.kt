package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.api.LoanRequest
import com.pinjam.peminjaman.api.RetrofitClient
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangDetailScreen(barang: Barang, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

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

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        val token = tokenManager.getToken()
                        if (token != null) {
                            isLoading = true
                            errorMessage = null
                            try {
                                // Default return plan: 7 days from now
                                val calendar = Calendar.getInstance()
                                calendar.add(Calendar.DAY_OF_YEAR, 7)
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val rencanaKembali = sdf.format(calendar.time)

                                val response = RetrofitClient.instance.createLoan(
                                    "Bearer $token",
                                    LoanRequest(
                                        idAset = (barang.id.toIntOrNull() ?: 0),
                                        rencanaKembali = rencanaKembali,
                                        catatan = "Pinjam dari Aplikasi"
                                    )
                                )
                                
                                if (response.isSuccessful) {
                                    successMessage = "Peminjaman berhasil diajukan!"
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    errorMessage = "Gagal meminjam: ${response.code()} $errorBody"
                                }
                            } catch (e: Exception) {
                                errorMessage = e.localizedMessage
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = barang.tersedia && !isLoading && successMessage == null
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (successMessage != null) "Selesai" else if (barang.tersedia) "Ajukan Peminjaman" else "Barang Tidak Tersedia")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarangDetailScreenPreview() {
    PeminjamanTheme {
        BarangDetailScreen(
            barang = Barang(
                id = "1",
                barcode = "AST-001",
                nama = "Proyektor",
                kategori = "Elektronik",
                deskripsi = "Epson Ruang A",
                tersedia = true
            ),
            onBack = {}
        )
    }
}
