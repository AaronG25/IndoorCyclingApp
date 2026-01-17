package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.ui.BleScanScreen
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

private val blePermissionLauncher =
    registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            startBleScan()
        }
    }

    private fun hasBlePermissions(): Boolean {
    val scan = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH_SCAN
    ) == PackageManager.PERMISSION_GRANTED

    val connect = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH_CONNECT
    ) == PackageManager.PERMISSION_GRANTED

    return scan && connect
}

private fun requestBlePermissions() {
    blePermissionLauncher.launch(
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    )
}

private fun startBleScan() {
    bleManager.startScan()
}





class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleManager = BleCadenceManager(this)

        setContent {
            BleScanScreen(
                bleManager = bleManager,
                onDeviceConnected = { }
            )
        }
    }
}
