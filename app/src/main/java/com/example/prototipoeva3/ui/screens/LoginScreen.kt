package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prototipoeva3.R

@Composable
fun LoginScreen(
    onSignIn: (email: String, password: String) -> Unit,
    isLoading: Boolean,
    message: String?,
    connectivityMessage: String,
    permissionInformation: String,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF42A5F5),
            Color(0xFF1E88E5),
            Color(0xFF0D47A1),
        ),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title: SENSOR DE PROXIMIDAD
            Text(
                text = "SENSOR DE\nPROXIMIDAD",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    letterSpacing = 1.5.sp,
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            // Floating Card Container for the form
            Card(
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    // Connection Status Badge
                    val isConnected = !connectivityMessage.contains("No hay", ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isConnected) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isConnected) Color(0xFF43A047) else Color(0xFFE53935)),
                        )
                        Text(
                            text = if (isConnected) "Conectado a Internet" else "Sin conexión a Internet",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isConnected) Color(0xFF2E7D32) else Color(0xFFC62828),
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.email_label)) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.password_label)) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                    )

                    if (message != null) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Button(
                        onClick = {
                            onSignIn(email, password)
                            password = ""
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF43A047),
                        ),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                            )
                        } else {
                            Text(
                                text = "Iniciar sesión",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}
