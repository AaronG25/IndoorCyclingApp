package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.ui.BleScanScreen
import com.indoorcycling.app.ui.HomeScreen
import com.indoorcycling.app.ui.theme.IndoorCyclingTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleManager = BleCadenceManager(this)

        setContent {
            IndoorCyclingTheme {

                var paired by remember { mutableStateOf(false) }

                if (!paired) {
                    BleScanScreen(
                        bleManager = bleManager,
                        onDeviceConnected = { paired = true }
                    )
                } else {
                    HomeScreen(bleManager = bleManager)
                }
            }
        }
    }
}
