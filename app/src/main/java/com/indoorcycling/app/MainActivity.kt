package com.indoorcycling.app

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
                // Simulation : on "ajoute" un capteur
                state.value = state.value.copy(hasSensor = true)
            },

            onStartStop = {
                state.value = state.value.copy(
                    isRunning = !state.value.isRunning
                )
            }
        )
    }
}

