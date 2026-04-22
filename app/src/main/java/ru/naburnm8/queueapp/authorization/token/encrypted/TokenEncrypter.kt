package ru.naburnm8.queueapp.authorization.token.encrypted

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager


class TokenEncrypter (context: Context) {
    private val aead: Aead

    private companion object {
        private val ASSOCIATED_DATA = "auth_tokens".encodeToByteArray()

    }

    init {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "auth_keyset", "auth_keyset_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://auth_queue_master_key")
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(value: String): String {
        val ciphertext = aead.encrypt(
            value.toByteArray(Charsets.UTF_8),
            ASSOCIATED_DATA
        )

        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }

    fun decrypt(value: String): String {
        val bytes = Base64.decode(value, Base64.NO_WRAP)
        val plaintext = aead.decrypt(bytes, ASSOCIATED_DATA)
        return plaintext.toString(Charsets.UTF_8)
    }
}