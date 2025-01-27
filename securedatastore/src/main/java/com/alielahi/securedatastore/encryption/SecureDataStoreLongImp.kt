package com.alielahi.securedatastore.encryption

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alielahi.securedatastore.storeData.DataStoreValue
import com.alielahi.securedatastore.utils.fromBase64
import com.alielahi.securedatastore.utils.getBase64String
import com.alielahi.securedatastore.utils.toByteArray
import com.alielahi.securedatastore.utils.toLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecureDataStoreLongImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<Long> {
    override suspend fun setData(key: String, data: Long) {
        return if (wantUseMutex)
            mutex.withLock {
                putLong(key, data)
            }
        else
            putLong(key, data)
    }

    override suspend fun getData(key: String): Long? {
        return if (wantUseMutex)
            mutex.withLock {
                getLong(key)
            }
        else
            getLong(key)
    }

    override fun asFlowable(key: String): Flow<Long> {
        return dataStore.data.map {
            getData(key) ?: -1L
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

    private suspend fun getLong(key: String): Long? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey]?: return null

        if (base64Data == "")
            return null

        val encryptedData =  base64Data.fromBase64()//fromBase64(base64Data)
        val decryptedData = encryption.decryption(encryptedData)
        return decryptedData.toLong()
    }

    private suspend fun putLong(key: String, data: Long) {
        val byteArrayData = data.toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()//getBase64(encryptedData)

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }
}