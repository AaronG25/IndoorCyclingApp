package com.indoorcycling.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: SessionState,
    onStartStop: () -> Unit,
    onAddSensor: () -> Unit
) {
    Column {

        Text(
            text = if (state.hasSensor)
                "Capteur connecté"
            else
                "Aucun capteur"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onAddSensor) {
            Text("Ajouter un capteur")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onStartStop) {
            Text(if (state.isRunning) "Arrêter" else "Démarrer")
        }
    }
}
