package com.app.abeslink.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Repository for securely storing and retrieving credentials
 * Uses EncryptedSharedPreferences for secure storage
 */
class CredentialsRepository(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "abeslink_credentials",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    /**
     * Save credentials securely
     */
    fun saveCredentials(username: String, password: String) {
        encryptedPrefs.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    /**
     * Get saved username
     */
    fun getUsername(): String? {
        return encryptedPrefs.getString(KEY_USERNAME, null)
    }

    /**
     * Get saved password
     */
    fun getPassword(): String? {
        return encryptedPrefs.getString(KEY_PASSWORD, null)
    }

    /**
     * Check if credentials are saved
     */
    fun hasCredentials(): Boolean {
        return !getUsername().isNullOrBlank() && !getPassword().isNullOrBlank()
    }

    /**
     * Clear all saved credentials
     */
    fun clearCredentials() {
        encryptedPrefs.edit()
            .remove(KEY_USERNAME)
            .remove(KEY_PASSWORD)
            .apply()
    }

    /**
     * Get both credentials as a pair
     */
    fun getCredentials(): Pair<String?, String?> {
        return Pair(getUsername(), getPassword())
    }
}
