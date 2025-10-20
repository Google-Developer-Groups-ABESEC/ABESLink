package com.app.abeslink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.abeslink.ui.credentials.CredentialsScreen
import com.app.abeslink.ui.dashboard.DashboardScreen
import com.app.abeslink.ui.theme.ABESLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ABESLinkTheme {
                ABESLinkApp()
            }
        }
    }
}

@Composable
fun ABESLinkApp() {
    val navController = rememberNavController()

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
                    // TODO: Navigate to Settings screen
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
    }
}