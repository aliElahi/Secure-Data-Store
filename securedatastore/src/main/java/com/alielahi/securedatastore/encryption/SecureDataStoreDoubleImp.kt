package com.alielahi.securedatastore.encryption

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alielahi.securedatastore.storeData.DataStoreValue
import com.alielahi.securedatastore.utils.fromBase64
import com.alielahi.securedatastore.utils.getBase64String
import com.alielahi.securedatastore.utils.toByteArray
import com.alielahi.securedatastore.utils.toDouble
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecureDataStoreDoubleImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<Double> {

    override suspend fun setData(key: String, data: Double) {
        if (wantUseMutex) {
            mutex.withLock {
                putDouble(key, data)
            }
        } else {
            putDouble(key, data)
        }
    }

    override suspend fun getData(key: String): Double? {
        return if (wantUseMutex) {
            mutex.withLock {
                getDouble(key)
            }
        } else {
            getDouble(key)
        }
    }

    override fun asFlowable(key: String): Flow<Double> {
        return dataStore.data.map {
            getData(key) ?: 0.0
        }
    }

    override suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    override suspend fun deleteKey(key: String) {
        val keyPreferences = stringPreferencesKey(key)
        dataStore.edit {
            it.remove(keyPreferences)
        }
    }

    private suspend fun getDouble(key: String): Double? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey]?: return null

        if (base64Data == "")
            return null

        val encryptedData = base64Data.fromBase64()
        val decryptedData = encryption.decryption(encryptedData)
        return decryptedData.toDouble()
    }

    private suspend fun putDouble(key: String, data: Double) {
        val byteArrayData = data.toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }
}