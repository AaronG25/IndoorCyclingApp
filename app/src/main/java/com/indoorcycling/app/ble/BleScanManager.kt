package com.indoorcycling.app.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid

class BleScanManager(
    context: Context,
    private val onDeviceFound: (BleDevice) -> Unit
) {

    private val scanner: BluetoothLeScanner? =
        BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {
            val device = result.device
            onDeviceFound(
                BleDevice(
                    name = device.name,
                    address = device.address
                )
            )
        }
    }

    fun startScan() {
        val filter = ScanFilter.Builder()
            .setServiceUuid(
                ParcelUuid(BleConstants.CYCLING_CADENCE_SERVICE)
            )
            .build()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    fun stopScan() {
        scanner?.stopScan(scanCallback)
    }
}
