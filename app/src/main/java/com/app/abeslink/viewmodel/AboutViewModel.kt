package com.app.abeslink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for About Screen
 */
class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    companion object {
        const val APP_VERSION = "1.0.0"
        const val BUILD_NUMBER = "1"
        const val GITHUB_REPO = "https://github.com/Google-Developer-Groups-ABESEC/ABESLink/"
        const val GITHUB = "https://github.com/Google-Developer-Groups-ABESEC/"
        const val REPORT_BUG_URL = "https://github.com/Google-Developer-Groups-ABESEC/ABESLink/issues"
        const val FEEDBACK_EMAIL = "dsc@abes.ac.in"
    }
}

/**
 * UI State for About Screen
 */
data class AboutUiState(
    val appVersion: String = AboutViewModel.APP_VERSION,
    val buildNumber: String = AboutViewModel.BUILD_NUMBER
)
