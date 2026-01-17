package com.indoorcycling.app.ui

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.data.BlePrefs
import kotlinx.coroutines.launch

@Composable
fun BleScanScreen(
    bleManager: BleCadenceManager,
    onDeviceConnected: () -> Unit
) {
    val devices = remember { mutableStateListOf<BluetoothDevice>() }
    var scanning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = {
                devices.clear()
                scanning = true
                bleManager.startScan { device ->
                    if (!devices.any { it.address == device.address }) {
                        devices.add(device)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (scanning)
                    "Recherche en cours..."
                else
                    "Ajouter un capteur de cadence"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(devices) { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            bleManager.stopScan()
                            bleManager.connect(device)
                            scanning = false

                            scope.launch {
                                BlePrefs.saveDeviceAddress(
                                    bleManager.context,
                                    device.address
                                )
                            }

                            onDeviceConnected()
                        }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = device.name ?: "Capteur inconnu")
                        Text(
                            text = device.address,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
