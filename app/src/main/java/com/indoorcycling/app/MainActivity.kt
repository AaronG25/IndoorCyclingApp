package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.ui.BleScanScreen

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
