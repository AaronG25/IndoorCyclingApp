package com.indoorcycling.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.indoorcycling.app.ui.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IndoorCyclingTheme {
                val state = remember {
                    mutableStateOf(SessionUiState())
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
