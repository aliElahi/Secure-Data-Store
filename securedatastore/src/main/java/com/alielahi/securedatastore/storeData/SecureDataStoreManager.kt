package com.alielahi.securedatastore.storeData

import android.content.Context
import androidx.annotation.Keep

@Keep
object SecureDataStoreManager {

    fun getSecureDataStore(context: Context): SecureDataStore =
        SecureDataStore.create(context = context)
}