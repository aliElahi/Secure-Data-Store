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

class SecureDataStoreStringImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<String> {

    override suspend fun setData(key: String, data: String) {
        if (wantUseMutex)
            mutex.withLock {
                putString(key = key, data = data)
            }
        else
            putString(key = key, data = data)
    }

    override suspend fun getData(key: String): String? {
        return if (wantUseMutex)
            mutex.withLock {
                getString(key)
            }
        else
            getString(key)
    }

    override fun asFlowable(key: String): Flow<String> {
        return dataStore.data.map {
            getData(key) ?: ""
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


    private suspend fun putString(key: String, data: String) {
        val byteArrayData = data.toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()//getBase64(encryptedData)

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }

    private suspend fun getString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey]

        if (base64Data.isNullOrEmpty())
            return null

        val encryptedData =  base64Data.fromBase64()//fromBase64(base64Data.toString())
        val decryptedData = encryption.decryption(encryptedData)
        return String(decryptedData)
    }
}