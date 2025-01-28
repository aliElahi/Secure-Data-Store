package com.alielahi.securedatastore.storeData

import android.content.Context
import com.alielahi.securedatastore.encryption.Encryption
import com.alielahi.securedatastore.encryption.SecureDataStoreBooleanImp
import com.alielahi.securedatastore.encryption.SecureDataStoreDoubleImp
import com.alielahi.securedatastore.encryption.SecureDataStoreFloatImp
import com.alielahi.securedatastore.encryption.SecureDataStoreIntImp
import com.alielahi.securedatastore.encryption.SecureDataStoreLongImp
import com.alielahi.securedatastore.encryption.SecureDataStoreStringImp
import com.alielahi.securedatastore.encryption.SecureElementEncryptionImp
import kotlinx.coroutines.sync.Mutex

interface SecureDataStore {

    fun setString(key: String, value: String)

    fun getString(key: String): String?


    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String): Boolean?


    fun setInt(key: String, value: Int)

    fun getInt(key: String): Int?


    fun setDouble(key: String, value: Double)

    fun getDouble(key: String): Double?


    fun setLong(key: String, value: Long)

    fun getLong(key: String): Long?


    fun setFloat(key: String, value: Float)

    fun getFloat(key: String): Float?


    companion object {
        fun create(context: Context): SecureDataStore {

            val encryption: Encryption = SecureElementEncryptionImp()

            val intDataStore: DataStoreValue<Int> = SecureDataStoreIntImp(
                dataStore = context.dataStore,
                encryption = encryption,
                mutex = Mutex()
            )
            val doubleDataStore: DataStoreValue<Double> = SecureDataStoreDoubleImp(
                dataStore = context.dataStore,
                encryption = encryption,
                mutex = Mutex()
            )

            val booleanDataStore: DataStoreValue<Boolean> = SecureDataStoreBooleanImp(
                dataStore = context.dataStore,
                encryption = encryption,
                mutex = Mutex()
            )

            val floatDataStore: DataStoreValue<Float> = SecureDataStoreFloatImp(
                dataStore = context.dataStore,
                encryption = encryption,
                mutex = Mutex()
            )

            val longDataStore: DataStoreValue<Long> = SecureDataStoreLongImp(
                dataStore = context.dataStore,
                encryption = encryption,
                mutex = Mutex()
            )

            val stringDataStore: DataStoreValue<String> = SecureDataStoreStringImp(
                dataStore = context.dataStore ,
                encryption = encryption ,
                mutex = Mutex()
            )

            return SecureDataStoreImp(
                intDataStore = intDataStore,
                doubleDataStore = doubleDataStore,
                booleanDataStore = booleanDataStore,
                floatDataStore = floatDataStore,
                longDataStore = longDataStore ,
                stringDataStore = stringDataStore
            )

        }
    }

}