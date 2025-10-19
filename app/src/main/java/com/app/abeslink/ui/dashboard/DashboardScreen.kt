package com.app.abeslink.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.abeslink.ui.theme.ABESLinkTheme
import com.app.abeslink.viewmodel.*

/**
 * Dashboard Screen - Main screen showing connection status and recent logs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onNavigateToCredentials: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        floatingActionButton = {
            AnimatedFloatingActionButton(
                isLoading = uiState.isTestingConnection,
                onClick = { viewModel.testConnection() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Summary
            item {
                HeaderSummaryCard(
                    ssid = uiState.ssid,
                    isAutoLoginEnabled = uiState.isAutoLoginEnabled,
                    lastLoginTime = uiState.lastLoginTime,
                    lastLoginStatus = uiState.lastLoginStatus
                )
            }

            // Connection Status Card
            item {
                AnimatedConnectionStatusCard(
                    connectionStatus = uiState.connectionStatus,
                    isLoading = uiState.isLoading
                )
            }

            // Action Buttons
            item {
                ActionButtonsRow(
                    isLoading = uiState.isLoading,
                    onLoginClick = { viewModel.loginNow() },
                    onCredentialsClick = onNavigateToCredentials,
                    onSettingsClick = onNavigateToSettings
                )
            }

            // Recent Logs Section
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Log Items
            items(uiState.recentLogs) { log ->
                LogItemCard(
                    logEntry = log,
                    onClick = { viewModel.showLogDetails(log) }
                )
            }

            // Empty state
            if (uiState.recentLogs.isEmpty()) {
                item {
                    EmptyLogsState()
                }
            }
        }
    }

    // Log Details Bottom Sheet
    if (uiState.selectedLog != null) {
        LogDetailsBottomSheet(
            logEntry = uiState.selectedLog!!,
            onDismiss = { viewModel.dismissLogDetails() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    contentDescription = "App Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "ABESLink",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun HeaderSummaryCard(
    ssid: String,
    isAutoLoginEnabled: Boolean,
    lastLoginTime: String,
    lastLoginStatus: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // SSID
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Router,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = ssid,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

            // Auto-login status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isAutoLoginEnabled) Icons.Filled.CheckCircle else Icons.Outlined.Cancel,
                        contentDescription = null,
                        tint = if (isAutoLoginEnabled) Color(0xFF4CAF50) else Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Auto-login: ${if (isAutoLoginEnabled) "ON" else "OFF"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // Last login info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$lastLoginTime • $lastLoginStatus",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedConnectionStatusCard(
    connectionStatus: ConnectionStatus,
    isLoading: Boolean
) {
    // Pulse animation for connecting state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(connectionStatus).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Animated Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(if (isLoading) scale else 1f)
                        .clip(CircleShape)
                        .background(getStatusColor(connectionStatus).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getStatusIcon(connectionStatus),
                        contentDescription = null,
                        tint = getStatusColor(connectionStatus),
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(if (isLoading) rotation else 0f)
                    )
                }

                // Status Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = getStatusText(connectionStatus),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(connectionStatus)
                    )
                    Text(
                        text = getStatusDescription(connectionStatus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtonsRow(
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onCredentialsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Login Now Button
        Button(
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isLoading) "Logging in..." else "Login Now")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Credentials Button
        OutlinedButton(
            onClick = onCredentialsClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Outlined.Key,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Credentials")
        }

        // Settings Button
        OutlinedButton(
            onClick = onSettingsClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings")
        }
    }
}

@Composable
fun LogItemCard(
    logEntry: LogEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Log Type Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getLogTypeColor(logEntry.type).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getLogTypeIcon(logEntry.type),
                    contentDescription = null,
                    tint = getLogTypeColor(logEntry.type),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Log Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = logEntry.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = logEntry.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Chevron
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "View details",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailsBottomSheet(
    logEntry: LogEntry,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(getLogTypeColor(logEntry.type).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getLogTypeIcon(logEntry.type),
                        contentDescription = null,
                        tint = getLogTypeColor(logEntry.type),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "Log Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = logEntry.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Divider()

            // Message
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Message",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = logEntry.message,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Type
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = logEntry.type.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = getLogTypeColor(logEntry.type)
                )
            }

            // Close Button
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AnimatedFloatingActionButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_scale"
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.scale(if (isLoading) scale else 1f),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Speed,
                contentDescription = "Test Connection",
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
fun EmptyLogsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Text(
            text = "No activity yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            text = "Your recent connection logs will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

// Helper Functions
@Composable
fun getStatusColor(status: ConnectionStatus): Color {
    return when (status) {
        ConnectionStatus.CONNECTED -> Color(0xFF4CAF50)
        ConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ConnectionStatus.CONNECTING -> MaterialTheme.colorScheme.primary
        ConnectionStatus.CAPTIVE_PORTAL -> Color(0xFFFF9800)
        ConnectionStatus.FAILED -> Color(0xFFF44336)
    }
}

@Composable
fun getStatusIcon(status: ConnectionStatus): ImageVector {
    return when (status) {
        ConnectionStatus.CONNECTED -> Icons.Filled.CheckCircle
        ConnectionStatus.DISCONNECTED -> Icons.Filled.WifiOff
        ConnectionStatus.CONNECTING -> Icons.Filled.Sync
        ConnectionStatus.CAPTIVE_PORTAL -> Icons.Filled.Warning
        ConnectionStatus.FAILED -> Icons.Filled.Error
    }
}

fun getStatusText(status: ConnectionStatus): String {
    return when (status) {
        ConnectionStatus.CONNECTED -> "Connected"
        ConnectionStatus.DISCONNECTED -> "Disconnected"
        ConnectionStatus.CONNECTING -> "Connecting..."
        ConnectionStatus.CAPTIVE_PORTAL -> "Captive Portal"
        ConnectionStatus.FAILED -> "Connection Failed"
    }
}

fun getStatusDescription(status: ConnectionStatus): String {
    return when (status) {
        ConnectionStatus.CONNECTED -> "You're online and ready to go"
        ConnectionStatus.DISCONNECTED -> "Not connected to any network"
        ConnectionStatus.CONNECTING -> "Establishing connection"
        ConnectionStatus.CAPTIVE_PORTAL -> "Login required to access internet"
        ConnectionStatus.FAILED -> "Unable to establish connection"
    }
}

@Composable
fun getLogTypeColor(type: LogType): Color {
    return when (type) {
        LogType.SUCCESS -> Color(0xFF4CAF50)
        LogType.ERROR -> Color(0xFFF44336)
        LogType.INFO -> MaterialTheme.colorScheme.primary
        LogType.WARNING -> Color(0xFFFF9800)
    }
}

@Composable
fun getLogTypeIcon(type: LogType): ImageVector {
    return when (type) {
        LogType.SUCCESS -> Icons.Filled.CheckCircle
        LogType.ERROR -> Icons.Filled.Error
        LogType.INFO -> Icons.Filled.Info
        LogType.WARNING -> Icons.Filled.Warning
    }
}

// Previews
@Preview(showBackground = true, name = "Dashboard Light")
@Composable
fun DashboardScreenPreview() {
    ABESLinkTheme(darkTheme = false) {
        DashboardScreen()
    }
}

@Preview(showBackground = true, name = "Dashboard Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardScreenDarkPreview() {
    ABESLinkTheme(darkTheme = true) {
        DashboardScreen()
    }
}
