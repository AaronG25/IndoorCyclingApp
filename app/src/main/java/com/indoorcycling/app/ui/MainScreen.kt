package com.indoorcycling.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indoorcycling.app.ble.BleDevice

@Composable
fun MainScreen(
    state: SessionState,
    onAddSensor: () -> Unit,
    onDeviceSelected: (BleDevice) -> Unit,
    onStartStop: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = when {
                state.selectedDevice != null ->
                    "Capteur : ${state.selectedDevice.name ?: "Inconnu"}"
                state.isScanning -> "Recherche de capteurs…"
                else -> "Aucun capteur"
            }
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = onAddSensor) {
            Text(if (state.isScanning) "Arrêter le scan" else "Ajouter un capteur")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(state.devices) { device ->
                DeviceRow(device, onDeviceSelected)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onStartStop) {
            Text(if (state.isRunning) "Arrêter" else "Démarrer")
        }
    }
}

@Composable
private fun DeviceRow(
    device: BleDevice,
    onClick: (BleDevice) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(device) }
            .padding(8.dp)
    ) {
        Text(device.name ?: "Capteur sans nom")
        Text(device.address)
    }
}
