package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prototipoeva3.parking.AlertRecord
import com.example.prototipoeva3.parking.FirebaseParkingRepository
import com.example.prototipoeva3.parking.ParkingRecord
import com.example.prototipoeva3.parking.ParkingRepository
import kotlinx.coroutines.delay

@Composable
fun SecurityMonitorScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    parkingRepository: ParkingRepository = remember { FirebaseParkingRepository() }
) {
    var parkingRecords by remember { mutableStateOf<List<ParkingRecord>>(emptyList()) }
    var alerts by remember { mutableStateOf<List<AlertRecord>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Auto-refresh data every 3 seconds to simulate real-time monitoring
    LaunchedEffect(Unit) {
        while (true) {
            isLoading = parkingRecords.isEmpty() && alerts.isEmpty()
            val parkingResult = parkingRepository.getRecentParkingRecords()
            val alertResult = parkingRepository.getRecentAlerts()

            if (parkingResult.isSuccess && alertResult.isSuccess) {
                parkingRecords = parkingResult.getOrDefault(emptyList())
                alerts = alertResult.getOrDefault(emptyList())
                errorMessage = null
            } else {
                errorMessage = "Error al sincronizar con Firestore"
            }
            isLoading = false
            delay(3000L)
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1565C0),
            Color(0xFF0D47A1),
            Color(0xFF002171)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with title and sign out button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Panel de Seguridad",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Monitoreo en tiempo real del vehículo",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Button(
                    onClick = onSignOut,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Salir", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val latestRecord = parkingRecords.firstOrNull()

                // Latest vehicle status card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (latestRecord?.estado) {
                            "PELIGRO" -> Color(0xFFC62828)
                            "ADVERTENCIA" -> Color(0xFFEF6C00)
                            else -> Color(0xFF2E7D32)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (latestRecord?.estado == "PELIGRO" || latestRecord?.estado == "ADVERTENCIA") {
                                Text(text = "⚠️", fontSize = 20.sp)
                            }
                            Text(
                                text = "ESTADO: ${latestRecord?.estado ?: "SIN DATOS"}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Distancia actual: ${latestRecord?.distancia ?: 0} cm",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Modo: ${latestRecord?.ubicacionModo ?: "N/A"} | Conductor: ${latestRecord?.userEmail ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Yellow,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Recent System Alerts Section (Cambios Bruscos / Advertencias)
                if (alerts.isNotEmpty()) {
                    Text(
                        text = "⚠️ Alertas del Sistema (Cambios Bruscos / Riesgo)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB300),
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.35f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(alerts) { alert ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEBEE)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🚨", fontSize = 22.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = alert.mensaje,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC62828)
                                        )
                                        Text(
                                            text = "Destinatario: ${alert.securityUserEmail}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Parking History Feed Header
                Text(
                    text = "Historial de Mediciones (En Vivo)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                // Feed List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.55f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(parkingRecords) { record ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.95f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (record.estado == "PELIGRO" || record.estado == "ADVERTENCIA") {
                                            Text(text = "⚠️", fontSize = 16.sp)
                                        }
                                        Text(
                                            text = "Distancia: ${record.distancia} cm (${record.estado})",
                                            fontWeight = FontWeight.Bold,
                                            color = when (record.estado) {
                                                "PELIGRO" -> Color(0xFFC62828)
                                                "ADVERTENCIA" -> Color(0xFFEF6C00)
                                                else -> Color(0xFF0D47A1)
                                            }
                                        )
                                    }
                                    Text(
                                        text = record.ubicacionModo,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "Conductor: ${record.userEmail}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                                if (record.nota.isNotBlank()) {
                                    Text(
                                        text = "Nota: ${record.nota}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
