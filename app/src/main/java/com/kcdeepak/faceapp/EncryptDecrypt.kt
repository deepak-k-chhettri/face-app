package com.kcdeepak.faceapp

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptDecrypt {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    fun getKey():SecretKey{
        val existingKey = keyStore.getEntry("secret",null) as? SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    fun createKey():SecretKey{
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder("secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

     val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE,getKey())
    }

    private fun getDecryptCipherForIv(iv:ByteArray):Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE,getKey(),IvParameterSpec(iv))
        }
    }

    fun encrypt(bytes:ByteArray):ByteArray{

        return encryptCipher.doFinal(bytes)
    }

    fun decrypt(encryptedCipher: ByteArray,IV:ByteArray):ByteArray{
        try{
            return getDecryptCipherForIv(IV).doFinal(encryptedCipher)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return encryptedCipher
    }

    companion object{
        const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}