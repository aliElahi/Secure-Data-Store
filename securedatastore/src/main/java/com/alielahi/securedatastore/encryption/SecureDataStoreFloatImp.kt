package com.alielahi.securedatastore.encryption

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alielahi.securedatastore.storeData.DataStoreValue
import com.alielahi.securedatastore.utils.fromBase64
import com.alielahi.securedatastore.utils.getBase64String
import com.alielahi.securedatastore.utils.toByteArray
import com.alielahi.securedatastore.utils.toFloat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecureDataStoreFloatImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<Float> {
    override suspend fun setData(key: String, data: Float) {

        if (wantUseMutex) {
            mutex.withLock {
                putFloat(key, data)
            }
        } else {
            putFloat(key, data)
        }
    }

    override suspend fun getData(key: String): Float? {
        return if (wantUseMutex) {
            mutex.withLock {
                getFloat(key)
            }
        } else
            getFloat(key)
    }

    override fun asFlowable(key: String): Flow<Float> {
        return dataStore.data.map {
            getData(key) ?: -1F
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

    private suspend fun putFloat(key: String, data: Float) {
        val byteArrayData = data.toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }

    private suspend fun getFloat(key: String): Float? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey]

        if (base64Data == "")
            return null

        val encryptedData = base64Data.fromBase64()
        val decryptedData = encryption.decryption(encryptedData)
        return decryptedData.toFloat()
    }
}