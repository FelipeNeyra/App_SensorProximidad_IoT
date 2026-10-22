package com.example.prototipoeva3.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.prototipoeva3.R
import com.example.prototipoeva3.parking.FirebaseParkingRepository
import com.example.prototipoeva3.parking.ParkingRepository
import kotlinx.coroutines.launch

@Composable
fun ValuesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    parkingRepository: ParkingRepository = remember { FirebaseParkingRepository() },
) {
    var distance by remember { mutableStateOf(50f) } // 0 to 100 cm
    var isTracking by remember { mutableStateOf(false) }
    var autoSaveEnabled by remember { mutableStateOf(true) }
    var selectedSensorLocation by remember { mutableStateOf("Frontal") }
    var parkingNote by remember { mutableStateOf("") }
    var saveStatusMessage by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isRedActive = distance <= 33f
    val isYellowActive = distance > 33f && distance <= 66f
    val isGreenActive = distance > 66f

    val redColor by animateColorAsState(
        if (isRedActive) Color(0xFFE53935) else Color(0xFF5C1D1D),
        label = "red",
    )
    val yellowColor by animateColorAsState(
        if (isYellowActive) Color(0xFFFFB300) else Color(0xFF5C4A1D),
        label = "yellow",
    )
    val greenColor by animateColorAsState(
        if (isGreenActive) Color(0xFF43A047) else Color(0xFF1D5C20),
        label = "green",
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF42A5F5),
            Color(0xFF1E88E5),
            Color(0xFF0D47A1),
        ),
    )

    val defaultSavedSuccessPattern = stringResource(R.string.saved_firestore_success, "")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top section: Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onBack,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(
                        stringResource(R.string.back_to_configuration),
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Center content: Title & Traffic light
            Text(
                text = stringResource(R.string.values_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            // Traffic light housing (Red, Yellow, Green)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E1E1E),
                shadowElevation = 8.dp,
                modifier = Modifier.padding(4.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(redColor),
                    )
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(yellowColor),
                    )
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(greenColor),
                    )
                }
            }

            // Distance value display
            Text(
                text = stringResource(R.string.distance_format, distance.toInt()),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            // Tracking and Firestore Status Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isTracking -> Color(0xFFFF8F00)
                        isSaving -> Color(0xFF1565C0)
                        saveStatusMessage != null -> Color(0xFF2E7D32)
                        else -> Color(0x33FFFFFF)
                    },
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = when {
                            isTracking -> stringResource(
                                R.string.tracking_active_status,
                                distance.toInt(),
                            )
                            isSaving -> stringResource(R.string.saving_firestore_status)
                            saveStatusMessage != null -> saveStatusMessage!!
                            else -> stringResource(R.string.tracking_idle_status)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            }

            // Interactive Controls Section (Rúbrica 2.1.1.2)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.sensor_simulator_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1),
                    )

                    // 1. Switch: Auto-save toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.auto_save_label),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )
                        Switch(
                            checked = autoSaveEnabled,
                            onCheckedChange = { autoSaveEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF1E88E5),
                            ),
                        )
                    }

                    // 2. RadioButtons: Sensor Location Selection
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.sensor_location_title),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedSensorLocation == "Frontal",
                                    onClick = { selectedSensorLocation = "Frontal" },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1E88E5)),
                                )
                                Text(
                                    text = stringResource(R.string.sensor_frontal),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedSensorLocation == "Trasero",
                                    onClick = { selectedSensorLocation = "Trasero" },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1E88E5)),
                                )
                                Text(
                                    text = stringResource(R.string.sensor_trasero),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }

                    // 3. OutlinedTextField (EditText): Parking Note / Location
                    OutlinedTextField(
                        value = parkingNote,
                        onValueChange = { parkingNote = it },
                        label = { Text(stringResource(R.string.parking_note_label)) },
                        placeholder = { Text(stringResource(R.string.parking_note_hint)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1E88E5),
                            focusedLabelColor = Color(0xFF1E88E5),
                        ),
                    )

                    // 4. Slider: Proximity Distance Variation
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            value = distance,
                            onValueChange = { newDistance ->
                                distance = newDistance
                                isTracking = true
                            },
                            onValueChangeFinished = {
                                isTracking = false
                                if (autoSaveEnabled) {
                                    coroutineScope.launch {
                                        isSaving = true
                                        val result = parkingRepository.saveParkingRecord(
                                            distanceCm = distance,
                                            ubicacionModo = selectedSensorLocation,
                                            nota = parkingNote,
                                        )
                                        isSaving = false
                                        saveStatusMessage = result.fold(
                                            onSuccess = { record ->
                                                defaultSavedSuccessPattern.format(record.id)
                                            },
                                            onFailure = {
                                                "Error al guardar en Firestore: ${it.localizedMessage}"
                                            },
                                        )
                                    }
                                }
                            },
                            valueRange = 0f..100f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF1E88E5),
                                activeTrackColor = Color(0xFF1E88E5),
                            ),
                        )
                    }
                }
            }
        }
    }
}
