package com.indoorcycling.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("ble_prefs")

object BlePrefs {

    private val DEVICE_ADDRESS = stringPreferencesKey("device_address")

    suspend fun saveDeviceAddress(context: Context, address: String) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_ADDRESS] = address
        }
    }

    suspend fun getDeviceAddress(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[DEVICE_ADDRESS]
    }
}
