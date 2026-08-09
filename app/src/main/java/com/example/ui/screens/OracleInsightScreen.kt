package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun OracleInsightScreen(
    viewModel: SpiritualGuardViewModel,
    modifier: Modifier = Modifier
) {
    val oracleQuery by viewModel.oracleQuery.collectAsState()
    val oracleResult by viewModel.oracleResult.collectAsState()
    val isOracleLoading by viewModel.isOracleLoading.collectAsState()

    val scrollState = rememberScrollState()

    val suggestedQuestions = listOf(
        "How do I guard my energy around negative environments?",
        "What spiritual practice will restore my emotional peace today?",
        "How can I let go of anxiety about the future?",
        "What sacred mantra will strengthen my core boundaries?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicIndigo)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("oracle_insight_screen")
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CosmicPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Oracle AI",
                    tint = SereneCyan,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Spiritual Oracle",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = CelestialWhite,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Personalized wisdom & energy protection guidance",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CelestialWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpiritualGold.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Seek Wisdom or Reflect",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SoftGold,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = oracleQuery,
                    onValueChange = { viewModel.setOracleQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag("oracle_input_field"),
                    placeholder = {
                        Text(
                            text = "Share what's on your heart or ask for guidance...",
                            color = CelestialWhite.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CosmicNavy,
                        unfocusedContainerColor = CosmicNavy,
                        focusedBorderColor = SpiritualGold,
                        unfocusedBorderColor = CelestialWhite.copy(alpha = 0.2f),
                        focusedTextColor = CelestialWhite,
                        unfocusedTextColor = CelestialWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.askOracle() },
                    enabled = oracleQuery.isNotBlank() && !isOracleLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("ask_oracle_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpiritualGold,
                        contentColor = CosmicIndigo
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    if (isOracleLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = CosmicIndigo,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Channeling Wisdom...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Seek Spiritual Insight",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Suggested Prompt Chips
        Text(
            text = "Suggested Questions",
            style = MaterialTheme.typography.labelMedium.copy(
                color = CelestialWhite.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            suggestedQuestions.forEach { prompt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setOracleQuery(prompt)
                            viewModel.askOracle()
                        }
                        .testTag("suggested_prompt_chip"),
                    colors = CardDefaults.cardColors(containerColor = CosmicNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Prompt",
                            tint = SereneCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CelestialWhite,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Oracle Result View
        if (oracleResult.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("oracle_result_card"),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SereneCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Guidance",
                            tint = SereneTeal,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Spiritual Guard Response",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = SoftGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = oracleResult,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CelestialWhite,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "✓ Automatically recorded in your Spiritual Journal",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SereneCyan,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
