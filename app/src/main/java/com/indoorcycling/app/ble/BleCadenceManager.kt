package com.indoorcycling.app.ble

import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class BleCadenceManager(private val context: Context) {

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val scanner = bluetoothAdapter.bluetoothLeScanner

    private var gatt: BluetoothGatt? = null

    private var lastCrankRevs: Int? = null
    private var lastEventTime: Int? = null

    private val _cadenceRpm = MutableStateFlow(0)
    val cadenceRpm: StateFlow<Int> = _cadenceRpm

    /* ===================== SCAN ===================== */

    fun startScan(onDeviceFound: (BluetoothDevice) -> Unit) {
        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(BleConstants.CSC_SERVICE_UUID))
            .build()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner.startScan(listOf(filter), settings, object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                onDeviceFound(result.device)
            }
        })
    }

    fun stopScan() {
        scanner.stopScan(object : ScanCallback() {})
    }

    /* ===================== CONNECTION ===================== */

    fun connect(device: BluetoothDevice) {
        gatt = device.connectGatt(context, false, gattCallback)
    }

    fun disconnect() {
        gatt?.disconnect()
        gatt = null
    }

    /* ===================== GATT ===================== */

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val service = gatt.getService(BleConstants.CSC_SERVICE_UUID)
            val characteristic = service?.getCharacteristic(
                BleConstants.CSC_MEASUREMENT_UUID
            )

            characteristic?.let {
                gatt.setCharacteristicNotification(it, true)

                val descriptor = it.getDescriptor(
                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
                )
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == BleConstants.CSC_MEASUREMENT_UUID) {
                parseCadence(characteristic.value)
            }
        }
    }

    /* ===================== PARSING ===================== */

    private fun parseCadence(data: ByteArray) {
        /*
         CSC Measurement format (crank only) :
         Flags (1 byte)
         Cumulative Crank Revolutions (2 bytes)
         Last Crank Event Time (2 bytes, 1/1024s)
        */

        if (data.size < 5) return

        val crankRevs =
            ((data[2].toInt() and 0xFF) or ((data[3].toInt() and 0xFF) shl 8))
        val eventTime =
            ((data[4].toInt() and 0xFF) or ((data[5].toInt() and 0xFF) shl 8))

        if (lastCrankRevs != null && lastEventTime != null) {
            val revDelta = crankRevs - lastCrankRevs!!
            val timeDelta =
                (eventTime - lastEventTime!! + 65536) % 65536

            if (timeDelta > 0 && revDelta > 0) {
                val timeSeconds = timeDelta / 1024.0
                val rpm = (revDelta / timeSeconds * 60.0).roundToInt()
                _cadenceRpm.value = rpm
            }
        }

        lastCrankRevs = crankRevs
        lastEventTime = eventTime
    }
}
