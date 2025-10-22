package com.app.abeslink.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.abeslink.ui.theme.ABESLinkTheme
import com.app.abeslink.viewmodel.ProbeInterval
import com.app.abeslink.viewmodel.SettingsViewModel

/**
 * Settings Screen - App configuration and preferences
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar for clear success
    LaunchedEffect(uiState.showClearSuccessMessage) {
        if (uiState.showClearSuccessMessage) {
            snackbarHostState.showSnackbar(
                message = "All data cleared successfully ✓",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        topBar = {
            SettingsTopBar(onNavigateBack = onNavigateBack)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            SettingsHeader()

            // General Settings Section
            SettingsSectionTitle(title = "General")
            
            // Dark Mode Toggle
            SettingsToggleItem(
                icon = Icons.Filled.DarkMode,
                title = "Dark Mode",
                subtitle = "Switch between light and dark theme",
                checked = uiState.isDarkModeEnabled,
                onCheckedChange = { 
                    viewModel.toggleDarkMode(it)
                    onDarkModeToggle(it)
                }
            )

            // Auto-login Toggle
            SettingsToggleItem(
                icon = Icons.Filled.Login,
                title = "Auto-login",
                subtitle = "Automatically login when captive portal is detected",
                checked = uiState.isAutoLoginEnabled,
                onCheckedChange = viewModel::toggleAutoLogin
            )

            // Foreground Service Toggle
            SettingsToggleItem(
                icon = Icons.Filled.Notifications,
                title = "Foreground Service",
                subtitle = "Keep app running in background (may impact battery)",
                checked = uiState.isForegroundServiceEnabled,
                onCheckedChange = viewModel::toggleForegroundService,
                warningText = if (uiState.isForegroundServiceEnabled) "⚠️ May increase battery usage" else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Network Settings Section
            SettingsSectionTitle(title = "Network")

            // Probe Interval
            ProbeIntervalSelector(
                selectedInterval = uiState.probeInterval,
                onIntervalSelected = viewModel::setProbeInterval
            )

            // Probe URL
            ProbeUrlSetting(
                url = uiState.probeUrl,
                isEditable = uiState.isProbeUrlEditable,
                onToggleEdit = viewModel::toggleProbeUrlEdit,
                onUrlChange = viewModel::updateProbeUrl,
                onSave = viewModel::saveProbeUrl
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Data Management Section
            SettingsSectionTitle(title = "Data Management")

            // Clear All Data Button
            ClearDataButton(
                isClearing = uiState.isClearing,
                onClick = viewModel::showClearDataDialog
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Clear Data Confirmation Dialog
    if (uiState.showClearDataDialog) {
        ClearDataDialog(
            onConfirm = viewModel::clearAllData,
            onDismiss = viewModel::dismissClearDataDialog
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun SettingsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icon with gradient
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Text(
            text = "App Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    warningText: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Title and subtitle
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Switch
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            }

            // Warning text
            if (warningText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = warningText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.padding(start = 64.dp)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProbeIntervalSelector(
    selectedInterval: ProbeInterval,
    onIntervalSelected: (ProbeInterval) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Title and subtitle
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Probe Interval",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "How often to check network status",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedInterval.displayName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ProbeInterval.entries.forEach { interval ->
                        DropdownMenuItem(
                            text = { Text(interval.displayName) },
                            onClick = {
                                onIntervalSelected(interval)
                                expanded = false
                            },
                            leadingIcon = {
                                if (interval == selectedInterval) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProbeUrlSetting(
    url: String,
    isEditable: Boolean,
    onToggleEdit: (Boolean) -> Unit,
    onUrlChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Link,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Title and subtitle
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Probe URL",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "URL used to detect captive portal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Edit toggle
                IconButton(onClick = { onToggleEdit(!isEditable) }) {
                    Icon(
                        imageVector = if (isEditable) Icons.Filled.Close else Icons.Filled.Edit,
                        contentDescription = if (isEditable) "Cancel" else "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // URL TextField
            OutlinedTextField(
                value = url,
                onValueChange = onUrlChange,
                enabled = isEditable,
                placeholder = { Text("Enter probe URL") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                singleLine = true
            )

            // Save button (only shown when editing)
            AnimatedVisibility(visible = isEditable) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save URL")
                    }
                }
            }
        }
    }
}

@Composable
fun ClearDataButton(
    isClearing: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF44336).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isClearing, onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF44336).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isClearing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFFF44336),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = null,
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Title and subtitle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isClearing) "Clearing Data..." else "Clear All Data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF44336)
                )
                Text(
                    text = "Reset app to default settings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Arrow icon
            if (!isClearing) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFF44336).copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun ClearDataDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF44336).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        title = {
            Text(
                text = "Clear All Data?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "This will reset all settings to default and clear saved credentials. This action cannot be undone.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Clear All",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

// Previews
@Preview(showBackground = true, name = "Settings Light")
@Composable
fun SettingsScreenPreview() {
    ABESLinkTheme(darkTheme = false) {
        SettingsScreen()
    }
}

@Preview(showBackground = true, name = "Settings Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenDarkPreview() {
    ABESLinkTheme(darkTheme = true) {
        SettingsScreen()
    }
}
