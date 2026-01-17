package com.indoorcycling.app.ble

import java.util.UUID

object BleConstants {

    // Cycling Speed and Cadence Service
    val CSC_SERVICE_UUID: UUID =
        UUID.fromString("00001816-0000-1000-8000-00805f9b34fb")

    // CSC Measurement characteristic
    val CSC_MEASUREMENT_UUID: UUID =
        UUID.fromString("00002a5b-0000-1000-8000-00805f9b34fb")
}
