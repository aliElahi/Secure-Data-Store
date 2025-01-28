package com.alielahi.securedatastore.encryption

import java.security.KeyStore

object KeyStoreManager
{

    fun getKeyStoreInstance(): KeyStore = KeyStore.getInstance(KEY_STORE_NAME).apply {
        load(null)
    }

    fun isKeyCreated(alias : String): Boolean {
        val ks = getKeyStoreInstance()
        return ks.isKeyEntry(alias)
    }

    private const val KEY_STORE_NAME = "AndroidKeyStore"
}