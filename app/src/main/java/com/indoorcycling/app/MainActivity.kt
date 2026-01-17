package com.indoorcycling.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.ui.MainScreen
import com.indoorcycling.app.ui.theme.Theme

class MainActivity : ComponentActivity() {

    private lateinit var bleManager: BleCadenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bleManager = BleCadenceManager(this)

        setContent {
            IndoorCyclingTheme {
                MainScreen(
                    onAddSensorClick = {
                        if (hasBlePermissions()) {
                            startBleScan()
                        } else {
                            requestBlePermissions()
                        }
                    }
                )
            }
        }
    }

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
}
