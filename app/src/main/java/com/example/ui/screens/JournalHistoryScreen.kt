package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.JournalEntry
import com.example.data.db.MeditationSession
import com.example.data.db.SavedMantra
import com.example.ui.theme.CardDark
import com.example.ui.theme.CelestialWhite
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.SereneCyan
import com.example.ui.theme.SereneTeal
import com.example.ui.theme.SoftGold
import com.example.ui.theme.SpiritualGold
import com.example.ui.viewmodel.SpiritualGuardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalHistoryScreen(
    viewModel: SpiritualGuardViewModel,
    modifier: Modifier = Modifier
) {
    val sessions by viewModel.sessions.collectAsState(initial = emptyList())
    val journalEntries by viewModel.journalEntries.collectAsState(initial = emptyList())
    val savedMantras by viewModel.savedMantras.collectAsState(initial = emptyList())

    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sessions (${sessions.size})", "Journal (${journalEntries.size})", "Mantras (${savedMantras.size})")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicIndigo)
            .testTag("journal_history_screen")
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = CosmicNavy,
            contentColor = SpiritualGold,
            indicator = { tabPositions ->
                if (selectedSubTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                        color = SpiritualGold
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSubTab == index) SpiritualGold else CelestialWhite.copy(alpha = 0.7f)
                            )
                        )
                    },
                    modifier = Modifier.testTag("journal_subtab_$index")
                )
            }
        }

        when (selectedSubTab) {
            0 -> SessionsList(sessions)
            1 -> JournalEntriesList(journalEntries, onDelete = { viewModel.deleteJournalEntry(it) })
            2 -> SavedMantrasList(savedMantras, onDelete = { viewModel.deleteSavedMantra(it) })
        }
    }
}

@Composable
fun SessionsList(sessions: List<MeditationSession>) {
    if (sessions.isEmpty()) {
        EmptyStateView(
            icon = Icons.Default.SelfImprovement,
            title = "No Completed Sessions Yet",
            subtitle = "Your completed guided practices and energy shields will be preserved here."
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions, key = { it.id }) { session ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("session_item_${session.id}"),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SpiritualGold.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = session.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGold
                                )
                            )
                            Text(
                                text = formatDate(session.dateTimestamp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CelestialWhite.copy(alpha = 0.5f)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Intent: ${session.intent} • ${session.durationMinutes} Mins",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SereneCyan,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "State: ${session.moodBefore} ➔ ${session.moodAfter}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CelestialWhite.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntriesList(
    entries: List<JournalEntry>,
    onDelete: (Long) -> Unit
) {
    if (entries.isEmpty()) {
        EmptyStateView(
            icon = Icons.Default.Book,
            title = "Your Spiritual Journal is Empty",
            subtitle = "Ask the Oracle or reflect on your day to create sacred journal entries."
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries, key = { it.id }) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("journal_item_${entry.id}"),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SereneTeal.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reflection / Query",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = SereneTeal,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            IconButton(onClick = { onDelete(entry.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Entry",
                                    tint = CelestialWhite.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = "“${entry.userReflection}”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CelestialWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "AI Guidance:",
                            style = MaterialTheme.typography.labelSmall.copy(color = SoftGold)
                        )

                        Text(
                            text = entry.aiGuidanceText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CelestialWhite.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedMantrasList(
    mantras: List<SavedMantra>,
    onDelete: (Long) -> Unit
) {
    if (mantras.isEmpty()) {
        EmptyStateView(
            icon = Icons.Default.Bookmark,
            title = "No Favorited Mantras",
            subtitle = "Tap the bookmark icon on today's sacred mantra to preserve it forever."
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mantras, key = { it.id }) { mantra ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mantra_item_${mantra.id}"),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SpiritualGold.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sacred Protection Mantra",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SoftGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            IconButton(onClick = { onDelete(mantra.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Mantra",
                                    tint = CelestialWhite.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = "“${mantra.mantraText}”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CelestialWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = mantra.meaningText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CelestialWhite.copy(alpha = 0.75f),
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(CosmicNavy),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = SpiritualGold,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = CelestialWhite,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = CelestialWhite.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
