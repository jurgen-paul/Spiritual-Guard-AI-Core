package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meditation_sessions")
data class MeditationSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val intent: String,
    val durationMinutes: Int,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val summary: String,
    val moodBefore: String,
    val moodAfter: String = "",
    val shieldEnergyGained: Int = 15
)

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userReflection: String,
    val aiGuidanceText: String,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val moodCategory: String,
    val assignedMantra: String = ""
)

@Entity(tableName = "saved_mantras")
data class SavedMantra(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mantraText: String,
    val meaningText: String,
    val category: String,
    val isFavorite: Boolean = true,
    val dateSaved: Long = System.currentTimeMillis()
)
