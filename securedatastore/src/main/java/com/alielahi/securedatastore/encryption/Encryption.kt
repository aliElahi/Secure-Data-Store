package com.alielahi.securedatastore.encryption

interface Encryption {
    fun encrypt(data: ByteArray) : ByteArray

    fun decryption(data: ByteArray) : ByteArray
}
