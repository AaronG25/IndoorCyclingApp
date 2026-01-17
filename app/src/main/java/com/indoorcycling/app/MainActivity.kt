package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.indoorcycling.app.ble.BleCadenceManager
import com.indoorcycling.app.ui.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IndoorCyclingTheme {

                val bleManager = remember {
                    BleCadenceManager(this)
                }

                var paired by remember { mutableStateOf(false) }
                val cadence = bleManager.cadenceRpm.collectAsState()

                val state = remember {
                    mutableStateOf(SessionUiState())
                }

                LaunchedEffect(cadence.value) {
                    state.value = state.value.copy(
                        cadence = cadence.value
                    )
                }

                if (!paired) {
                    BleScanScreen(
                        bleManager = bleManager,
                        onDeviceConnected = { paired = true }
                    )
                } else {
                    MainScreen(
                        state = state.value,
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
}
