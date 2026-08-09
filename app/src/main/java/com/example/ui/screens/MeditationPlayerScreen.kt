package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BreathingShield
import com.example.ui.theme.CardDark
import com.example.ui.theme.CelestialWhite
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.CosmicPurple
import com.example.ui.theme.SereneCyan
import com.example.ui.theme.SereneTeal
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SpiritualGold
import com.example.ui.viewmodel.MeditationStatus
import com.example.ui.viewmodel.SpiritualGuardViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeditationPlayerScreen(
    viewModel: SpiritualGuardViewModel,
    modifier: Modifier = Modifier
) {
    val selectedMood by viewModel.selectedMood.collectAsState()
    val selectedIntent by viewModel.selectedIntent.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val meditationStatus by viewModel.meditationStatus.collectAsState()
    val generatedScript by viewModel.generatedScript.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val currentBreathingPhase by viewModel.currentBreathingPhase.collectAsState()

    val scrollState = rememberScrollState()

    val moods = listOf(
        "Anxiety & Overwhelm",
        "Mental Exhaustion",
        "Stress & Pressure",
        "Seeking Inner Peace",
        "Gratitude & Calm",
        "Restless Mind"
    )

    val intents = listOf(
        "Protection Energy Shield",
        "Deep Heart Healing",
        "Tranquil Night Rest",
        "Mental Clarity & Focus",
        "Spiritual Grounding"
    )

    val durations = listOf(5, 10, 20)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicIndigo)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("meditation_player_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Title
        Text(
            text = "AI Meditation Sanctuary",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = CelestialWhite,
                fontSize = 22.sp
            )
        )
        Text(
            text = "Personalized guided practice tailored to your present state",
            style = MaterialTheme.typography.bodySmall.copy(
                color = CelestialWhite.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // If not playing, allow selecting mood/intent/duration
        if (meditationStatus == MeditationStatus.IDLE || meditationStatus == MeditationStatus.GENERATING) {
            // Mood Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "1. Current Emotional State",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftGold,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        moods.forEach { mood ->
                            val isSelected = mood == selectedMood
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setMood(mood) },
                                label = {
                                    Text(
                                        text = mood,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) CosmicIndigo else CelestialWhite
                                        )
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SpiritualGold,
                                    containerColor = CosmicNavy
                                ),
                                modifier = Modifier.testTag("mood_chip_${mood.take(5)}")
                            )
                        }
                    }
                }
            }

            // Intent Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "2. Sacred Intention",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftGold,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        intents.forEach { intent ->
                            val isSelected = intent == selectedIntent
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setIntent(intent) },
                                label = {
                                    Text(
                                        text = intent,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) CosmicIndigo else CelestialWhite
                                        )
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SereneTeal,
                                    containerColor = CosmicNavy
                                ),
                                modifier = Modifier.testTag("intent_chip_${intent.take(5)}")
                            )
                        }
                    }
                }
            }

            // Duration Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "3. Session Duration",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SoftGold,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        durations.forEach { duration ->
                            val isSelected = duration == selectedDuration
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) SpiritualGold else CosmicNavy)
                                    .border(
                                        1.dp,
                                        if (isSelected) SpiritualGold else CelestialWhite.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.setDuration(duration) }
                                    .padding(vertical = 10.dp)
                                    .testTag("duration_${duration}m"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$duration min",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CosmicIndigo else CelestialWhite
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Generate Button
            Button(
                onClick = { viewModel.generateMeditation() },
                enabled = meditationStatus != MeditationStatus.GENERATING,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("generate_meditation_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpiritualGold,
                    contentColor = CosmicIndigo
                ),
                shape = RoundedCornerShape(26.dp)
            ) {
                if (meditationStatus == MeditationStatus.GENERATING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = CosmicIndigo,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crafting AI Guided Script...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Generate",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Craft AI Guided Practice",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }

        // Active Player View (READY, PLAYING, PAUSED, FINISHED)
        if (meditationStatus != MeditationStatus.IDLE && meditationStatus != MeditationStatus.GENERATING) {
            Spacer(modifier = Modifier.height(12.dp))

            // Breathing Shield Canvas
            BreathingShield(
                phase = currentBreathingPhase,
                remainingSeconds = remainingSeconds,
                isPlaying = meditationStatus == MeditationStatus.PLAYING
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Player Control Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (meditationStatus == MeditationStatus.READY || meditationStatus == MeditationStatus.PAUSED) {
                    Button(
                        onClick = { viewModel.startMeditationSession() },
                        colors = ButtonDefaults.buttonColors(containerColor = SpiritualGold),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(60.dp)
                            .testTag("play_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Session",
                            tint = CosmicIndigo,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else if (meditationStatus == MeditationStatus.PLAYING) {
                    Button(
                        onClick = { viewModel.pauseMeditationSession() },
                        colors = ButtonDefaults.buttonColors(containerColor = SereneTeal),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(60.dp)
                            .testTag("pause_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause Session",
                            tint = CosmicIndigo,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                if (meditationStatus == MeditationStatus.PLAYING || meditationStatus == MeditationStatus.PAUSED) {
                    OutlinedButton(
                        onClick = { viewModel.stopMeditationSession() },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp)
                            .testTag("stop_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Session",
                            tint = CelestialWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (meditationStatus == MeditationStatus.FINISHED) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Finished",
                            tint = SpiritualGold,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Practice Complete",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CelestialWhite,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Your inner shield is strengthened. +20 Aura Energy Gained!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SoftGold,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.stopMeditationSession() },
                            colors = ButtonDefaults.buttonColors(containerColor = SpiritualGold)
                        ) {
                            Text("Return to Sanctuary", color = CosmicIndigo)
                        }
                    }
                }
            }

            // Script Reader Card
            if (generatedScript.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SpiritualGold.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Script",
                                tint = SpiritualGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Guided AI Meditation Script",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = SoftGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = generatedScript,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CelestialWhite.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
