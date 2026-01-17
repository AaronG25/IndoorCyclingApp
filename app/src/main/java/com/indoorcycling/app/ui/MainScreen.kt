package com.indoorcycling.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    state: SessionUiState,
    onStartStop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Metric(title = "Temps", value = formatTime(state.durationSeconds))
            Metric(title = "Fréquence cardiaque", value = "${state.heartRate} bpm")
            Metric(title = "Cadence", value = "${state.cadence} rpm")
            Metric(title = "Vitesse", value = String.format("%.1f km/h", state.speedKmh))
        }

        Button(
            onClick = onStartStop,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = if (state.isRunning) "Arrêter" else "Démarrer",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun Metric(title: String, value: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.headlineMedium)
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
