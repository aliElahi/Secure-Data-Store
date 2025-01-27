package com.alielahi.securedatastore.encryption

import java.security.KeyStore

object KeyStoreManager
{

    fun getKeyStoreInstance(): KeyStore = KeyStore.getInstance(keyStoreName).apply {
        load(null)
    }

    fun isKeyCreated(alias : String): Boolean {
        val ks = getKeyStoreInstance()
        return ks.isKeyEntry(alias)
    }

    private const val keyStoreName = "AndroidKeyStore"
}