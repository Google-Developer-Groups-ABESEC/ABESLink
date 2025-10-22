package com.app.abeslink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.abeslink.ui.credentials.CredentialsScreen
import com.app.abeslink.ui.dashboard.DashboardScreen
import com.app.abeslink.ui.settings.SettingsScreen
import com.app.abeslink.ui.theme.ABESLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ABESLinkApp()
        }
    }
}

@Composable
fun ABESLinkApp() {
    val navController = rememberNavController()
    var isDarkMode by remember { mutableStateOf(true) }

    ABESLinkTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "dashboard"
        ) {
            composable("dashboard") {
                DashboardScreen(
                    onNavigateToCredentials = {
                        navController.navigate("credentials")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }

            composable("credentials") {
                CredentialsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable("settings") {
                SettingsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onDarkModeToggle = { enabled ->
                        isDarkMode = enabled
                    }
                )
            }
        }
    }
}