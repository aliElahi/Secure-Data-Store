package com.alielahi.securedatastore.storeData

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val preferencesName = "com.ali.elahi.secureDataStore"

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = preferencesName)