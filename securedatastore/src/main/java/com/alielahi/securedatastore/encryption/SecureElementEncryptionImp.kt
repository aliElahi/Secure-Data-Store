package com.alielahi.securedatastore.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.alielahi.securedatastore.utils.toByteArray
import java.nio.ByteBuffer
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

 class SecureElementEncryptionImp : Encryption {

    override fun encrypt(data: ByteArray): ByteArray {
        val cipher = getCipher()
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val blockSize = cipher.blockSize.toByteArray()

        return blockSize + cipher.iv + cipher.doFinal(data)
    }

    override fun decryption(data: ByteArray): ByteArray {
        val cipher = getCipher()
        val key = getSecretKey()

        val blockSize = getBlockSize(data)

        val firstIndexOfIv = Int.SIZE_BYTES
        val lastIndexOfIv = firstIndexOfIv + blockSize

        val ivByte = data.copyOfRange(firstIndexOfIv , lastIndexOfIv)

        val encryptedData = data.copyOfRange(lastIndexOfIv, data.size)

        val iv = IvParameterSpec(ivByte)

        cipher.init(Cipher.DECRYPT_MODE, key, iv)

        return cipher.doFinal(encryptedData)
    }

    private fun createKeyPair() {

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE_NAME)

        val keyGenParamsSpec = KeyGenParameterSpec
            .Builder(ALIAS, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()

        keyGenerator.init(keyGenParamsSpec)
        keyGenerator.generateKey()

    }

    private fun isKeyCreated(): Boolean {
        return KeyStoreManager.isKeyCreated(alias = ALIAS)
    }

    private fun getSecretKey(): SecretKey {

        if (!isKeyCreated())
            createKeyPair()

        val keyStore = KeyStoreManager.getKeyStoreInstance()
        keyStore.load(null)

        val secretKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey

    }

    private fun getCipher() = Cipher.getInstance(CIPHER_TRANSFORM_STRING)

    private fun getBlockSize(bytes: ByteArray): Int {
        val byteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES)
        byteBuffer.put(bytes.copyOfRange(0 , Int.SIZE_BYTES))
        byteBuffer.rewind()
        return byteBuffer.int
    }

    companion object {
        const val ALIAS = "com.alielahi.securedatastore_app_alias"
        const val KEY_STORE_NAME = "AndroidKeyStore"

        const val CIPHER_TRANSFORM_STRING =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"

        val instance : Encryption by lazy {
            SecureElementEncryptionImp()
        }
        //fun create() : Encryption = SecureElementEncryptionImp()

    }

}