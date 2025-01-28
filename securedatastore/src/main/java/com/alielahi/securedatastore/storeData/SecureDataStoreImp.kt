package com.alielahi.securedatastore.storeData

import kotlinx.coroutines.runBlocking

class SecureDataStoreImp(
    private val intDataStore: DataStoreValue<Int>,
    private val booleanDataStore: DataStoreValue<Boolean>,
    private val floatDataStore: DataStoreValue<Float>,
    private val longDataStore: DataStoreValue<Long>,
    private val doubleDataStore: DataStoreValue<Double>,
    private val stringDataStore: DataStoreValue<String>
) : SecureDataStore {

    override fun setString(key: String, value: String) {
        runBlocking {
            stringDataStore.setData(key = key, data = value)
        }
    }

    override fun getString(key: String): String? {
        return runBlocking {
            return@runBlocking stringDataStore.getData(key = key)
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        runBlocking {
            booleanDataStore.setData(key = key, data = value)
        }
    }

    override fun getBoolean(key: String): Boolean? {
        return runBlocking {
            return@runBlocking booleanDataStore.getData(key = key)
        }
    }

    override fun setInt(key: String, value: Int) {
        runBlocking {
            intDataStore.setData(key = key, data = value)
        }
    }

    override fun getInt(key: String): Int? {
        return runBlocking {
            return@runBlocking intDataStore.getData(key = key)
        }
    }

    override fun setDouble(key: String, value: Double) {
        return runBlocking {
            return@runBlocking doubleDataStore.setData(key = key, data = value)
        }
    }

    override fun getDouble(key: String): Double? {
        return runBlocking {
            return@runBlocking doubleDataStore.getData(key = key)
        }
    }

    override fun setLong(key: String, value: Long) {
        runBlocking {
            longDataStore.setData(key = key, data = value)
        }
    }

    override fun getLong(key: String): Long? {
        return runBlocking {
            return@runBlocking longDataStore.getData(key = key)
        }
    }

    override fun setFloat(key: String, value: Float) {
        runBlocking {
            floatDataStore.setData(key = key, data = value)
        }
    }

    override fun getFloat(key: String): Float? {
        return runBlocking {
            return@runBlocking floatDataStore.getData(key = key)
        }
    }


}