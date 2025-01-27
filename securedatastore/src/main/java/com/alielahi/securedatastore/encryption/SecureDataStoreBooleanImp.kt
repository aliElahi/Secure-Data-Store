package com.alielahi.securedatastore.encryption

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alielahi.securedatastore.storeData.DataStoreValue
import com.alielahi.securedatastore.utils.fromBase64
import com.alielahi.securedatastore.utils.getBase64String
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecureDataStoreBooleanImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<Boolean> {
    override suspend fun setData(key: String, data: Boolean) {

        if (wantUseMutex) {
            mutex.withLock {
                putBoolean(key, data)
            }
        } else {
            putBoolean(key, data)
        }
    }

    override suspend fun getData(key: String): Boolean? {
        return if (wantUseMutex) {
            mutex.withLock {
                getBoolean(key)
            }
        } else
            getBoolean(key)
    }

    override fun asFlowable(key: String): Flow<Boolean> {
        return dataStore.data.map {
            getData(key) ?: false
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

    private suspend fun putBoolean(key: String, data: Boolean) {
        //val dataValue = data.toString()
        val byteArrayData = data.toString().toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }

    private suspend fun getBoolean(key: String): Boolean? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey]

        if (base64Data == "")
            return null

        val encryptedData =  base64Data.fromBase64()
        val decryptedData = encryption.decryption(encryptedData)
        val decryptedString = decryptedData.toString(Charsets.UTF_8)
        return decryptedString.toBoolean()
    }
}