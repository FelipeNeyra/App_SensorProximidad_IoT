package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prototipoeva3.R

@Composable
fun PermissionBlockedScreen(
    isIndefinite: Boolean,
    isInitialRequest: Boolean,
    onRetry: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.permission_request_title))
        Text(
            stringResource(
                when {
                    isIndefinite -> R.string.permission_blocked_indefinitely_message
                    isInitialRequest -> R.string.permission_request_message
                    else -> R.string.permission_denied_message
                }
            )
        )
        if (isIndefinite) {
            Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.permission_open_settings))
            }
        } else {
            Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(
                        if (isInitialRequest) {
                            R.string.permission_request_action
                        } else {
                            R.string.permission_retry
                        }
                    )
                )
            }
        }
    }
}
