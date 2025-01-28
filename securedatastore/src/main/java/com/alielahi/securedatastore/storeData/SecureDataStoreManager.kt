package com.alielahi.securedatastore.storeData

import android.content.Context

object SecureDataStoreManager {

    fun getSecureDataStore(context: Context): SecureDataStore =
        SecureDataStore.create(context = context)
}