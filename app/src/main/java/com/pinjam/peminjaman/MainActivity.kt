package com.pinjam.peminjaman

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinjam.peminjaman.ui.theme.PeminjamanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeminjamanTheme {
                PeminjamanApp()
            }
        }
    }
}

@Composable
fun PeminjamanApp() {
    var isLoggedIn by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoggedIn) {
            MainScreen(onLogout = { isLoggedIn = false })
        } else {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }

    var currentBerandaScreen by remember { mutableStateOf("home") }
    var selectedBarang by remember { mutableStateOf<Barang?>(null) }

    // Handle System Back Button
    BackHandler(enabled = selectedItem != 0 || currentBerandaScreen != "home") {
        if (selectedItem != 0) {
            selectedItem = 0
        } else {
            when (currentBerandaScreen) {
                "scan" -> currentBerandaScreen = "home"
                "list" -> currentBerandaScreen = "home"
                "return" -> currentBerandaScreen = "home"
                "detail" -> currentBerandaScreen = "list"
            }
        }
    }

    val items = listOf("Beranda", "Riwayat", "Profil")
    val icons = listOf(Icons.Default.Home, Icons.AutoMirrored.Filled.List, Icons.Default.Person)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectedItem != 0 || currentBerandaScreen == "home") {
                TopAppBar(title = { Text(items[selectedItem]) })
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            // Reset beranda state when switching tabs if desired
                            if (index != 0) {
                                currentBerandaScreen = "home"
                                selectedBarang = null
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedItem) {
                0 -> {
                    when (currentBerandaScreen) {
                        "home" -> BerandaScreen(
                            onNavigateToList = { currentBerandaScreen = "list" },
                            onNavigateToReturn = { currentBerandaScreen = "return" },
                            onNavigateToScan = { currentBerandaScreen = "scan" }
                        )

                        "scan" -> ScanScreen(
                            onBack = { currentBerandaScreen = "home" },
                            onScanResult = {
                                selectedBarang = it
                                currentBerandaScreen = "detail"
                            }
                        )

                        "list" -> BarangListScreen(
                            onBack = { currentBerandaScreen = "home" },
                            onItemClick = {
                                selectedBarang = it
                                currentBerandaScreen = "detail"
                            },
                            onScanClick = { currentBerandaScreen = "scan" }
                        )

                        "detail" -> selectedBarang?.let {
                            BarangDetailScreen(
                                barang = it,
                                onBack = { currentBerandaScreen = "list" }
                            )
                        }

                        "return" -> PengembalianScreen(
                            onBack = { currentBerandaScreen = "home" }
                        )
                    }
                }

                1 -> RiwayatScreen()
                2 -> ProfilScreen(onLogout = onLogout)
            }
        }
    }
}



@Composable
fun BerandaScreen(
    onNavigateToList: () -> Unit,
    onNavigateToReturn: () -> Unit,
    onNavigateToScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Pilih Layanan", style = MaterialTheme.typography.titleMedium)
        
        MenuCard(
            "Scan Barcode Pinjam",
            Icons.Default.QrCodeScanner,
            Modifier.fillMaxWidth(),
            onClick = onNavigateToScan
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MenuCard(
                "List Barang",
                Icons.Default.Inventory,
                Modifier.weight(1f),
                onClick = onNavigateToList
            )
            MenuCard(
                "Pengembalian",
                Icons.AutoMirrored.Filled.Assignment,
                Modifier.weight(1f),
                onClick = onNavigateToReturn
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = modifier.height(120.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, title, Modifier.size(32.dp), MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PeminjamanTheme {
        MainScreen(onLogout = {})
    }
}


@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    PeminjamanTheme {
        BerandaScreen(onNavigateToList = {}, onNavigateToReturn = {}, onNavigateToScan = {})
    }
}


@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun AppPreview() {
    PeminjamanTheme {
        PeminjamanApp()
    }
}

