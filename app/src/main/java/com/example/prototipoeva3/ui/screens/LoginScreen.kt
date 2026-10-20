package com.example.prototipoeva3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.login_title))
        Text(text = connectivityMessage)
        Text(text = permissionInformation)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.email_label)) },
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        if (message != null) Text(text = message)
        Button(
            onClick = {
                onSignIn(email, password)
                password = ""
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isLoading) CircularProgressIndicator()
            else Text(stringResource(R.string.continue_label))
        }
    }
}
