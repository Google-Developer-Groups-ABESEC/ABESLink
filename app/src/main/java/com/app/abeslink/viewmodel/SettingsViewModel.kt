package com.app.abeslink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Settings Screen
 * Manages app settings and preferences
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    /**
     * Load saved settings
     */
    private fun loadSettings() {
        // Load from SharedPreferences (mock for now)
        _uiState.value = SettingsUiState(
            isAutoLoginEnabled = true,
            isForegroundServiceEnabled = false,
            isDarkModeEnabled = true,
            probeInterval = ProbeInterval.THIRTY_MINUTES,
            probeUrl = "http://clients3.google.com/generate_204",
            isProbeUrlEditable = false
        )
    }

    /**
     * Toggle auto-login
     */
    fun toggleAutoLogin(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isAutoLoginEnabled = enabled)
        saveSettings()
    }

    /**
     * Toggle foreground service
     */
    fun toggleForegroundService(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isForegroundServiceEnabled = enabled)
        saveSettings()
    }

    /**
     * Toggle dark mode
     */
    fun toggleDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkModeEnabled = enabled)
        saveSettings()
    }

    /**
     * Set probe interval
     */
    fun setProbeInterval(interval: ProbeInterval) {
        _uiState.value = _uiState.value.copy(probeInterval = interval)
        saveSettings()
    }

    /**
     * Toggle probe URL editability
     */
    fun toggleProbeUrlEdit(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isProbeUrlEditable = enabled)
    }

    /**
     * Update probe URL
     */
    fun updateProbeUrl(url: String) {
        _uiState.value = _uiState.value.copy(probeUrl = url)
    }

    /**
     * Save probe URL
     */
    fun saveProbeUrl() {
        _uiState.value = _uiState.value.copy(isProbeUrlEditable = false)
        saveSettings()
    }

    /**
     * Show clear data confirmation
     */
    fun showClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = true)
    }

    /**
     * Dismiss clear data dialog
     */
    fun dismissClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = false)
    }

    /**
     * Clear all app data
     */
    fun clearAllData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isClearing = true,
                showClearDataDialog = false
            )

            // Simulate clearing data
            delay(1500)

            // Reset to defaults
            _uiState.value = SettingsUiState(
                isAutoLoginEnabled = false,
                isForegroundServiceEnabled = false,
                isDarkModeEnabled = false,
                probeInterval = ProbeInterval.THIRTY_MINUTES,
                probeUrl = "http://clients3.google.com/generate_204",
                isProbeUrlEditable = false,
                isClearing = false,
                showClearSuccessMessage = true
            )

            // Hide success message after 3 seconds
            delay(3000)
            _uiState.value = _uiState.value.copy(showClearSuccessMessage = false)
        }
    }

    /**
     * Save settings to storage
     */
    private fun saveSettings() {
        // TODO: Save to SharedPreferences
        viewModelScope.launch {
            delay(100) // Simulate save operation
        }
    }
}

/**
 * UI State for Settings Screen
 */
data class SettingsUiState(
    val isAutoLoginEnabled: Boolean = false,
    val isForegroundServiceEnabled: Boolean = false,
    val isDarkModeEnabled: Boolean = true,
    val probeInterval: ProbeInterval = ProbeInterval.THIRTY_MINUTES,
    val probeUrl: String = "http://clients3.google.com/generate_204",
    val isProbeUrlEditable: Boolean = false,
    val showClearDataDialog: Boolean = false,
    val isClearing: Boolean = false,
    val showClearSuccessMessage: Boolean = false
)

/**
 * Probe interval options
 */
enum class ProbeInterval(val minutes: Int, val displayName: String) {
    FIFTEEN_MINUTES(15, "15 minutes"),
    THIRTY_MINUTES(30, "30 minutes"),
    SIXTY_MINUTES(60, "60 minutes")
}
