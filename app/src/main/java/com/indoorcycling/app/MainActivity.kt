package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.indoorcycling.app.ui.*
import androidx.compose.runtime.collectAsState
import com.indoorcycling.app.ble.BleCadenceManager


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IndoorCyclingTheme {
                val state = val bleManager = remember {
    BleCadenceManager(this)
}

val cadence = bleManager.cadenceRpm.collectAsState()

val state = remember {
    mutableStateOf(SessionUiState())
}

LaunchedEffect(cadence.value) {
    state.value = state.value.copy(
        cadence = cadence.value
    )
}


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
