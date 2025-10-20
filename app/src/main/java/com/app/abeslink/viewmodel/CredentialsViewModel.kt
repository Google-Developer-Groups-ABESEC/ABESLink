package com.app.abeslink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.abeslink.data.CredentialsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Credentials Screen
 */
class CredentialsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CredentialsRepository(application.applicationContext)
    
    private val _uiState = MutableStateFlow(CredentialsUiState())
    val uiState: StateFlow<CredentialsUiState> = _uiState.asStateFlow()

    init {
        loadSavedCredentials()
    }

    /**
     * Load saved credentials from secure storage
     */
    private fun loadSavedCredentials() {
        val (username, password) = repository.getCredentials()
        _uiState.value = CredentialsUiState(
            username = username ?: "",
            password = password ?: "",
            isSaved = repository.hasCredentials()
        )
    }

    /**
     * Update username field
     */
    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            usernameError = null
        )
    }

    /**
     * Update password field
     */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    /**
     * Toggle password visibility
     */
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    /**
     * Validate and save credentials to secure storage
     */
    fun saveCredentials() {
        // Validate inputs
        val usernameError = when {
            _uiState.value.username.isBlank() -> "Username cannot be empty"
            _uiState.value.username.length < 12 -> "Username must be at least 12 characters"
            else -> null
        }

        val passwordError = when {
            _uiState.value.password.isBlank() -> "Password cannot be empty"
            _uiState.value.password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }

        if (usernameError != null || passwordError != null) {
            _uiState.value = _uiState.value.copy(
                usernameError = usernameError,
                passwordError = passwordError
            )
            return
        }

        // Save credentials to encrypted storage
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            
            // Small delay for UI feedback
            delay(500)
            
            // Save to encrypted shared preferences
            repository.saveCredentials(
                username = _uiState.value.username,
                password = _uiState.value.password
            )
            
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                isSaved = true,
                showSuccessMessage = true
            )

            // Hide success message after 3 seconds
            delay(3000)
            _uiState.value = _uiState.value.copy(showSuccessMessage = false)
        }
    }

    /**
     * Clear all credentials with confirmation
     */
    fun showClearConfirmation() {
        _uiState.value = _uiState.value.copy(showClearDialog = true)
    }

    fun dismissClearConfirmation() {
        _uiState.value = _uiState.value.copy(showClearDialog = false)
    }

    fun clearCredentials() {
        // Clear from encrypted storage
        repository.clearCredentials()
        
        // Reset UI state
        _uiState.value = CredentialsUiState(
            username = "",
            password = "",
            isSaved = false,
            showClearDialog = false
        )
    }
}

/**
 * UI State for Credentials Screen
 */
data class CredentialsUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val showSuccessMessage: Boolean = false,
    val showClearDialog: Boolean = false
)
