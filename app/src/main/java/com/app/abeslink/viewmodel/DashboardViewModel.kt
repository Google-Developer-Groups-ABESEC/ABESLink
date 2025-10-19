package com.app.abeslink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel for Dashboard Screen
 * Manages UI state and simulates login operations with mock data
 */
class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Initialize with mock data
        loadMockData()
    }

    private fun loadMockData() {
        _uiState.value = DashboardUiState(
            ssid = "ABES_WiFi_5G",
            isAutoLoginEnabled = true,
            lastLoginTime = "2 mins ago",
            lastLoginStatus = "Success",
            connectionStatus = ConnectionStatus.CONNECTED,
            recentLogs = listOf(
                LogEntry(
                    id = 1,
                    timestamp = getCurrentTime(),
                    message = "Login successful",
                    type = LogType.SUCCESS
                ),
                LogEntry(
                    id = 2,
                    timestamp = "10:23 AM",
                    message = "Captive portal detected",
                    type = LogType.INFO
                ),
                LogEntry(
                    id = 3,
                    timestamp = "10:20 AM",
                    message = "Connected to ABES_WiFi_5G",
                    type = LogType.SUCCESS
                )
            )
        )
    }

    /**
     * Simulates login operation with delay
     * WARNING scenarios:
     * - Slow network (latency > 3000ms)
     * - Weak signal strength
     * - Auto-login disabled
     * - Multiple retry attempts
     */
    fun loginNow() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                connectionStatus = ConnectionStatus.CONNECTING
            )

            // Simulate network delay
            val networkDelay = (1500..4000).random().toLong()
            delay(networkDelay)

            // Random scenario selection (70% success, 15% warning, 15% error)
            val scenario = (1..100).random()

            when {
                // SUCCESS: 70% chance
                scenario <= 70 -> {
                    val newLog = LogEntry(
                        id = System.currentTimeMillis(),
                        timestamp = getCurrentTime(),
                        message = "Login successful",
                        type = LogType.SUCCESS
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectionStatus = ConnectionStatus.CONNECTED,
                        lastLoginTime = "Just now",
                        lastLoginStatus = "Success",
                        recentLogs = listOf(newLog) + _uiState.value.recentLogs.take(2)
                    )
                }
                
                // WARNING: 15% chance - Various warning scenarios
                scenario in 71..85 -> {
                    val warningScenarios = listOf(
                        "Slow network detected - Login took ${networkDelay}ms",
                        "Weak signal strength - Connection may be unstable",
                        "Auto-login is disabled - Manual login required",
                        "Multiple retry attempts detected",
                        "Session timeout warning - Please re-login in 5 minutes",
                        "Network congestion detected - Reduced speed expected"
                    )
                    
                    val newLog = LogEntry(
                        id = System.currentTimeMillis(),
                        timestamp = getCurrentTime(),
                        message = warningScenarios.random(),
                        type = LogType.WARNING
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectionStatus = ConnectionStatus.CONNECTED,
                        lastLoginTime = "Just now",
                        lastLoginStatus = "Warning",
                        recentLogs = listOf(newLog) + _uiState.value.recentLogs.take(2)
                    )
                }
                
                // ERROR: 15% chance
                else -> {
                    val newLog = LogEntry(
                        id = System.currentTimeMillis(),
                        timestamp = getCurrentTime(),
                        message = "Login failed - Invalid credentials",
                        type = LogType.ERROR
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectionStatus = ConnectionStatus.FAILED,
                        lastLoginTime = "Just now",
                        lastLoginStatus = "Failed",
                        recentLogs = listOf(newLog) + _uiState.value.recentLogs.take(2)
                    )
                }
            }
        }
    }

    /**
     * Simulates connection test
     * WARNING scenarios:
     * - High latency (> 100ms)
     * - Packet loss detected
     * - DNS resolution slow
     */
    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTestingConnection = true)

            delay(1500)

            val latency = (10..200).random()
            val packetLoss = (0..10).random()
            
            // Determine log type based on test results
            val (message, logType) = when {
                latency > 150 || packetLoss > 5 -> {
                    "High latency detected: ${latency}ms - Network performance degraded" to LogType.WARNING
                }
                latency > 100 -> {
                    "Connection test warning - Latency: ${latency}ms (Slow)" to LogType.WARNING
                }
                packetLoss > 0 -> {
                    "Packet loss detected: ${packetLoss}% - Connection unstable" to LogType.WARNING
                }
                else -> {
                    "Connection test completed - Latency: ${latency}ms (Excellent)" to LogType.INFO
                }
            }

            val newLog = LogEntry(
                id = System.currentTimeMillis(),
                timestamp = getCurrentTime(),
                message = message,
                type = logType
            )

            _uiState.value = _uiState.value.copy(
                isTestingConnection = false,
                recentLogs = listOf(newLog) + _uiState.value.recentLogs.take(2)
            )
        }
    }

    /**
     * Expands log details
     */
    fun showLogDetails(logEntry: LogEntry) {
        _uiState.value = _uiState.value.copy(selectedLog = logEntry)
    }

    /**
     * Dismisses log details
     */
    fun dismissLogDetails() {
        _uiState.value = _uiState.value.copy(selectedLog = null)
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }
}

/**
 * UI State for Dashboard Screen
 */
data class DashboardUiState(
    val ssid: String = "Not Connected",
    val isAutoLoginEnabled: Boolean = false,
    val lastLoginTime: String = "Never",
    val lastLoginStatus: String = "Unknown",
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val isLoading: Boolean = false,
    val isTestingConnection: Boolean = false,
    val recentLogs: List<LogEntry> = emptyList(),
    val selectedLog: LogEntry? = null
)

/**
 * Connection status enum
 */
enum class ConnectionStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    CAPTIVE_PORTAL,
    FAILED
}

/**
 * Log entry data class
 */
data class LogEntry(
    val id: Long,
    val timestamp: String,
    val message: String,
    val type: LogType
)

/**
 * Log type enum
 */
enum class LogType {
    SUCCESS,
    ERROR,
    INFO,
    WARNING
}
