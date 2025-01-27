package com.alielahi.securedatastore.storeData

import kotlinx.coroutines.flow.Flow


interface DataStoreValue<T> {

    suspend fun setData(key : String, data :T)

    suspend fun getData(key: String) : T?

    fun asFlowable(key: String): Flow<T>

    suspend fun clear()

    suspend fun deleteKey(key: String)
}