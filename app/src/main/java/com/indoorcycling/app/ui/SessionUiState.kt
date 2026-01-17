package com.indoorcycling.app.ui

data class SessionUiState(
    val isRunning: Boolean = false,
    val durationSeconds: Int = 0,
    val heartRate: Int = 0,
    val cadence: Int = 0,
    val speedKmh: Float = 0f
)
