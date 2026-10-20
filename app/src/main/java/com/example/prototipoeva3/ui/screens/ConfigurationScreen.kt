package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.prototipoeva3.R

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.configuration_title), style = MaterialTheme.typography.titleLarge)
        
        Button(onClick = onBluetoothClick, modifier = Modifier.fillMaxWidth()) {
            Text(
                if (isBluetoothGranted) stringResource(R.string.bluetooth_granted)
                else stringResource(R.string.bluetooth_request)
            )
        }
        
        Button(onClick = onWifiClick, modifier = Modifier.fillMaxWidth()) {
            Text(
                if (isWifiGranted) stringResource(R.string.wifi_granted)
                else stringResource(R.string.wifi_request)
            )
        }
        
        Button(onClick = onValuesClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.values_label))
        }
        
        Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.logout_label))
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SecurityUserIcon()
            Text(stringResource(R.string.security_user_title))
        }
        
        OutlinedTextField(
            value = securityEmail,
            onValueChange = { securityEmail = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.security_user_email_label)) },
            singleLine = true
        )
        
        OutlinedTextField(
            value = securityPhone,
            onValueChange = { securityPhone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.security_user_phone_label)) },
            singleLine = true
        )
    }
}

@Composable
private fun SecurityUserIcon() {
    val iconColor = MaterialTheme.colorScheme.primary
    val iconDescription = stringResource(R.string.security_user_icon_description)
    Canvas(
        modifier = Modifier
            .size(36.dp)
            .semantics { contentDescription = iconDescription }
    ) {
        val centerX = size.width / 2
        drawCircle(color = iconColor, radius = size.width * 0.15f, center = androidx.compose.ui.geometry.Offset(centerX, size.height * 0.3f))
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
