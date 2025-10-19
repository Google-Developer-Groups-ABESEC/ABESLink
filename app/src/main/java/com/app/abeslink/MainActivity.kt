package com.app.abeslink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.abeslink.ui.dashboard.DashboardScreen
import com.app.abeslink.ui.theme.ABESLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ABESLinkTheme {
                DashboardScreen(
                    onNavigateToCredentials = {
                        // TODO: Navigate to Credentials screen
                    },
                    onNavigateToSettings = {
                        // TODO: Navigate to Settings screen
                    }
                )
            }
        }
    }
}