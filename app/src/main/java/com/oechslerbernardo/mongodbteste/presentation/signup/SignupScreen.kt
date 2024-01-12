package com.oechslerbernardo.mongodbteste.presentation.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    snackBar: SnackbarHostState,
    state: SignupState,
    onEvent: (SignUpEvent) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBar)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 40.dp))
            Text(
                text = "Create Your Account!",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(top = 60.dp))
            TextField(
                value = state.email,
                onValueChange = { onEvent(SignUpEvent.EmailChanged(it)) },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            TextField(
                value = state.name,
                onValueChange = { onEvent(SignUpEvent.NameChanged(it)) },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            TextField(
                value = state.password,
                onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Button(
                onClick = {
                    onEvent(SignUpEvent.OnSignupClick)
                }) {
                Text("Sign Up")
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}