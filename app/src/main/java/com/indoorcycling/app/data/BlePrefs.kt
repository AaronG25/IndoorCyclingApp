package com.indoorcycling.app.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("ble_prefs")

object BlePrefs {

    private val KEY_DEVICE_ADDRESS = stringPreferencesKey("cadence_device_address")

    fun deviceAddressFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[KEY_DEVICE_ADDRESS] }

    suspend fun saveDeviceAddress(context: Context, address: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DEVICE_ADDRESS] = address
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
