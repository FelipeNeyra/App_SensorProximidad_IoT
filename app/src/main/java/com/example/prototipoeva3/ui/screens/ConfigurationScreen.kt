package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfigurationScreen(
    isBluetoothGranted: Boolean,
    isWifiGranted: Boolean,
    onBluetoothClick: () -> Unit,
    onWifiClick: () -> Unit,
    onValuesClick: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var securityEmail by rememberSaveable { mutableStateOf("") }
    var securityPhone by rememberSaveable { mutableStateOf("") }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF42A5F5),
            Color(0xFF1E88E5),
            Color(0xFF0D47A1)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Icon
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    SecurityUserIconLarge()
                }
            }

            // MENU Title / Header separated and positioned cleanly
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp)
                    .padding(top = 12.dp),
                shape = CircleShape,
                color = Color(0xFF29B6F6),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "MENU",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                }
            }

            // Options section with generous spacing placed lower down
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Conexion WIFI Button
                Button(
                    onClick = onWifiClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (isWifiGranted) "Conexion WIFI (Activa)" else "Conexion WIFI",
                        color = Color(0xFF0D47A1),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Conexion Bluetooth Button
                Button(
                    onClick = onBluetoothClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (isBluetoothGranted) "Conexion Bluetooth (Activa)" else "Conexion Bluetooth",
                        color = Color(0xFF0D47A1),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Sensor (Valores) Button
                Button(
                    onClick = onValuesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Sensor",
                        color = Color(0xFF0D47A1),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Security user configuration fields placed further down with proper spacing
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contacto de Seguridad",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = securityEmail,
                    onValueChange = { securityEmail = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Correo de seguridad", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = securityPhone,
                    onValueChange = { securityPhone = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Teléfono de seguridad", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    )
                )
            }

            // Logout Button at the bottom
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .padding(top = 16.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SecurityUserIconLarge() {
    val iconColor = Color(0xFF1E88E5)
    androidx.compose.foundation.Canvas(
        modifier = Modifier.size(48.dp)
    ) {
        val centerX = size.width / 2
        drawCircle(color = iconColor, radius = size.width * 0.18f, center = androidx.compose.ui.geometry.Offset(centerX, size.height * 0.3f))
        drawArc(
            color = iconColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.45f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.7f, size.height * 0.5f),
            style = Stroke(width = size.width * 0.1f)
        )
    }
}
