package com.indoorcycling.app

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.indoorcycling.app.ui.MainScreen
import com.indoorcycling.app.ui.SessionState
import com.indoorcycling.app.ui.theme.IndoorCyclingTheme

class MainActivity : ComponentActivity() {

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
    if (hasBlePermissions()) {
        state.value = state.value.copy(hasSensor = true)
    } else {
        blePermissionGrantedCallback = {
            state.value = state.value.copy(hasSensor = true)
        }
        blePermissionLauncher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    }
}
,

            onStartStop = {
                state.value = state.value.copy(
                    isRunning = !state.value.isRunning
                )
            }
        )
    }
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

private val blePermissionLauncher =
    registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            onBlePermissionsGranted()
        }
    }

private fun onBlePermissionsGranted() {
    // Pour l’instant, on simule un capteur ajouté
    blePermissionGrantedCallback?.invoke()
}
private var blePermissionGrantedCallback: (() -> Unit)? = null
