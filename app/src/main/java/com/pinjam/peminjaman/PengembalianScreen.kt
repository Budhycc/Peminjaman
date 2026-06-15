package com.pinjam.peminjaman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.api.LoanResponse
import com.pinjam.peminjaman.api.RetrofitClient
import com.pinjam.peminjaman.api.ReturnRequest
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengembalianScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }
    
    var loans by remember { mutableStateOf<List<LoanResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun refreshLoans() {
        val token = tokenManager.getToken()
        if (token != null) {
            scope.launch {
                isLoading = true
                try {
                    val response = RetrofitClient.instance.getMyHistory("Bearer $token")
                    if (response.isSuccessful) {
                        loans = response.body()?.filter { it.status == "dipinjam" } ?: emptyList()
                    } else {
                        errorMessage = "Gagal memuat data"
                    }
                } catch (e: Exception) {
                    errorMessage = e.localizedMessage
                } finally {
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshLoans()
    }

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
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            } else if (loans.isEmpty()) {
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
                    items(loans) { loan ->
                        BorrowedItemRow(
                            loan = loan,
                            onReturn = {
                                scope.launch {
                                    val token = tokenManager.getToken()
                                    if (token != null) {
                                        try {
                                            val response = RetrofitClient.instance.createReturn(
                                                "Bearer $token",
                                                ReturnRequest(
                                                    idPeminjaman = loan.idPeminjaman ?: 0,
                                                    kondisiKembali = "baik",
                                                    catatan = "Dikembalikan via aplikasi"
                                                )
                                            )
                                            if (response.isSuccessful) {
                                                refreshLoans()
                                            }
                                        } catch (e: Exception) {
                                            // Handle error
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BorrowedItemRow(loan: LoanResponse, onReturn: () -> Unit) {
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
                    Text(loan.asset?.namaAset ?: "Aset", style = MaterialTheme.typography.titleMedium)
                    Text(loan.asset?.kategori ?: "Barang", style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = onReturn) {
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
