package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.components.TopHeaderBar
import com.example.ui.screens.JournalHistoryScreen
import com.example.ui.screens.MeditationPlayerScreen
import com.example.ui.screens.OracleInsightScreen
import com.example.ui.screens.SanctuaryScreen
import com.example.ui.theme.CelestialWhite
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.SpiritualGold
import com.example.ui.theme.SpiritualGuardTheme
import com.example.ui.viewmodel.SpiritualGuardViewModel

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: SpiritualGuardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpiritualGuardTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: SpiritualGuardViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val shieldEnergy by viewModel.shieldEnergy.collectAsState()

    val navItems = listOf(
        NavItem("Sanctuary", Icons.Default.Spa, "nav_sanctuary"),
        NavItem("AI Practice", Icons.Default.SelfImprovement, "nav_meditation"),
        NavItem("Oracle AI", Icons.Default.AutoAwesome, "nav_oracle"),
        NavItem("Journal", Icons.Default.Book, "nav_journal")
    )

    Scaffold(
        topBar = {
            TopHeaderBar(
                streakDays = streakDays,
                shieldEnergy = shieldEnergy
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CosmicNavy,
                contentColor = SpiritualGold
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(index) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) SpiritualGold else CelestialWhite.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (isSelected) SpiritualGold else CelestialWhite.copy(alpha = 0.6f)
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = CosmicIndigo
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CosmicIndigo)
        ) {
            Crossfade(
                targetState = currentTab,
                label = "screen_crossfade"
            ) { page ->
                when (page) {
                    0 -> SanctuaryScreen(
                        viewModel = viewModel,
                        onNavigateToMeditation = { viewModel.selectTab(1) },
                        onNavigateToOracle = { viewModel.selectTab(2) }
                    )
                    1 -> MeditationPlayerScreen(viewModel = viewModel)
                    2 -> OracleInsightScreen(viewModel = viewModel)
                    3 -> JournalHistoryScreen(viewModel = viewModel)
                }
            }
        }
    }
}
