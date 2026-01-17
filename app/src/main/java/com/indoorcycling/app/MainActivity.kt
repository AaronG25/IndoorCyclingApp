package com.indoorcycling.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.indoorcycling.app.ui.MainScreen
import com.indoorcycling.app.ui.SessionState
import com.indoorcycling.app.ui.theme.IndoorCyclingTheme
import com.indoorcycling.app.ble.BleScanManager
import com.indoorcycling.app.ble.BleDevice

private var scanManager: BleScanManager? = null


class MainActivity : ComponentActivity() {

    // Callback appelé après permissions OK
    private var onBlePermissionGranted: (() -> Unit)? = null

    // Launcher DOIT être ici (niveau Activity)
    private val blePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions: Map<String, Boolean> ->
            val granted = permissions.values.all { it }
            if (granted) {
                onBlePermissionGranted?.invoke()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IndoorCyclingTheme {

                val state = remember {
                    mutableStateOf(SessionState())
                }

                MainScreen(
                    state = state.value,

                    onAddSensor = {
    if (!state.value.isScanning) {
        state.value = state.value.copy(
            isScanning = true,
            devices = emptyList()
        )

        scanManager = BleScanManager(this) { device ->
            if (state.value.devices.none { it.address == device.address }) {
                state.value = state.value.copy(
                    devices = state.value.devices + device
                )
            }
        }

        scanManager?.startScan()

    } else {
        scanManager?.stopScan()
        state.value = state.value.copy(isScanning = false)
    }
},
onDeviceSelected = { device ->
    scanManager?.stopScan()
    state.value = state.value.copy(
        selectedDevice = device,
        isScanning = false,
        hasSensor = true
    )
},


                    onStartStop = {
                        state.value = state.value.copy(
                            isRunning = !state.value.isRunning
                        )
                    }
                )
            }
        }
    }

    private fun hasBlePermissions(): Boolean {
        val scanGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED

        val connectGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

        return scanGranted && connectGranted
    }
}
