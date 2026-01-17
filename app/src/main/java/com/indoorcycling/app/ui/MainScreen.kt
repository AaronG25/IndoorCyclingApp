package com.indoorcycling.app.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(
    state: SessionState,
    onStartStop: () -> Unit
) {
    Button(onClick = onStartStop) {
        Text(if (state.isRunning) "Arrêter" else "Démarrer")
    }
}
