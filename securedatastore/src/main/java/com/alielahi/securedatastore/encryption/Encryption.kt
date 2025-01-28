package com.alielahi.securedatastore.encryption

import androidx.annotation.Keep

@Keep
interface Encryption {
    fun encrypt(data: ByteArray) : ByteArray

    fun decryption(data: ByteArray) : ByteArray
}
