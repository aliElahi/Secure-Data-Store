package com.alielahi.securedatastore.encryption

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alielahi.securedatastore.storeData.DataStoreValue
import com.alielahi.securedatastore.utils.fromBase64
import com.alielahi.securedatastore.utils.getBase64String
import com.alielahi.securedatastore.utils.toByteArray
import com.alielahi.securedatastore.utils.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecureDataStoreIntImp(
    private val dataStore: DataStore<Preferences>,
    private val encryption: Encryption,
    private val mutex: Mutex,
    private val wantUseMutex: Boolean = false
) : DataStoreValue<Int> {

    override suspend fun setData(key: String, data: Int) {

        if (wantUseMutex) {
            mutex.withLock {
                putInt(key, data)
            }
        } else {
            putInt(key, data)
        }
    }

    override suspend fun getData(key: String): Int? {

        return if (wantUseMutex) {
            mutex.withLock {
                getInt(key)
            }
        } else {
            getInt(key)
        }
    }

    override fun asFlowable(key: String): Flow<Int> {
        return dataStore.data.map {
            getData(key) ?: -1
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

    private suspend fun getInt(key: String): Int? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        val base64Data = preference[dataStoreKey] ?: return null

        if (base64Data == "")
        return null


        val decoded = base64Data.fromBase64()

        val decryptedData = encryption.decryption(decoded)

        return decryptedData.toInt()
    }

    private suspend fun putInt(key: String, data: Int) {
        val byteArrayData = data.toByteArray()
        val encryptedData = encryption.encrypt(byteArrayData)
        val base64Data = encryptedData.getBase64String()

        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = base64Data
        }
    }

}