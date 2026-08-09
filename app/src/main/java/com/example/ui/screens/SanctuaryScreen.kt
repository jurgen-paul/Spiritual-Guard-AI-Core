package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CardDark
import com.example.ui.theme.CelestialWhite
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.SereneCyan
import com.example.ui.theme.SereneTeal
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SpiritualGold
import com.example.ui.viewmodel.SpiritualGuardViewModel

@Composable
fun SanctuaryScreen(
    viewModel: SpiritualGuardViewModel,
    onNavigateToMeditation: () -> Unit,
    onNavigateToOracle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyMantra by viewModel.dailyMantra.collectAsState()
    val dailyMantraMeaning by viewModel.dailyMantraMeaning.collectAsState()
    val isMantraLoading by viewModel.isMantraLoading.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val shieldEnergy by viewModel.shieldEnergy.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicIndigo)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
            .testTag("sanctuary_screen")
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, SpiritualGold.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_meditation),
                contentDescription = "Hero Meditation Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CosmicNavy.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shield",
                        tint = SpiritualGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Daily Spiritual Sanctuary",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SoftGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Shield Your Energy & Guard Your Mind",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = CelestialWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        }

        // Daily Mantra Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("daily_mantra_card"),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpiritualGold.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Mantra AI",
                            tint = SpiritualGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Today's Sacred Mantra",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = SoftGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { viewModel.refreshDailyMantra() },
                            modifier = Modifier.testTag("refresh_mantra_button")
                        ) {
                            if (isMantraLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = SpiritualGold,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Mantra",
                                    tint = CelestialWhite.copy(alpha = 0.8f)
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.saveCurrentMantra() },
                            modifier = Modifier.testTag("save_mantra_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Save Mantra",
                                tint = SpiritualGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "“$dailyMantra”",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = CelestialWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        lineHeight = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dailyMantraMeaning,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CelestialWhite.copy(alpha = 0.75f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Guided Meditations Section Title
        Text(
            text = "Quick Guided Shield Practice",
            style = MaterialTheme.typography.titleMedium.copy(
                color = CelestialWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickMeditationCard(
                title = "3 Min Grounding",
                intent = "Quick Aura Shield",
                icon = Icons.Default.Spa,
                duration = 3,
                accentColor = SpiritualGold,
                onClick = {
                    viewModel.setDuration(3)
                    viewModel.setIntent("Quick Aura Shield")
                    viewModel.generateMeditation()
                    onNavigateToMeditation()
                },
                modifier = Modifier.weight(1f)
            )

            QuickMeditationCard(
                title = "5 Min Protection",
                intent = "Protection Energy Shield",
                icon = Icons.Default.Shield,
                duration = 5,
                accentColor = SereneTeal,
                onClick = {
                    viewModel.setDuration(5)
                    viewModel.setIntent("Protection Energy Shield")
                    viewModel.generateMeditation()
                    onNavigateToMeditation()
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickMeditationCard(
                title = "10 Min Deep Sleep",
                intent = "Tranquil Night Rest",
                icon = Icons.Default.Bedtime,
                duration = 10,
                accentColor = SereneCyan,
                onClick = {
                    viewModel.setDuration(10)
                    viewModel.setIntent("Tranquil Night Rest")
                    viewModel.generateMeditation()
                    onNavigateToMeditation()
                },
                modifier = Modifier.weight(1f)
            )

            QuickMeditationCard(
                title = "20 Min Deep Focus",
                intent = "Emotional Heart Healing",
                icon = Icons.Default.SelfImprovement,
                duration = 20,
                accentColor = SoftGold,
                onClick = {
                    viewModel.setDuration(20)
                    viewModel.setIntent("Emotional Heart Healing")
                    viewModel.generateMeditation()
                    onNavigateToMeditation()
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Ask Spiritual Oracle AI Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onNavigateToOracle() }
                .testTag("oracle_banner_card"),
            colors = CardDefaults.cardColors(containerColor = CosmicNavy),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SereneCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CosmicPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Spiritual Oracle",
                        tint = SereneCyan,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Seek Spiritual Oracle Insights",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = CelestialWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "Ask questions about emotional protection, life reflections, or spiritual balance.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CelestialWhite.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuickMeditationCard(
    title: String,
    intent: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    duration: Int,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("quick_meditation_${duration}m"),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = CelestialWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$duration Mins • AI Guided",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CelestialWhite.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            )
        }
    }
}
