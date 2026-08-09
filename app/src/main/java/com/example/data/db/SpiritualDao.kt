package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpiritualDao {

    // Meditation Sessions
    @Query("SELECT * FROM meditation_sessions ORDER BY dateTimestamp DESC")
    fun getAllSessions(): Flow<List<MeditationSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: MeditationSession): Long

    @Query("SELECT COUNT(*) FROM meditation_sessions")
    fun getSessionCount(): Flow<Int>

    // Journal Entries
    @Query("SELECT * FROM journal_entries ORDER BY dateTimestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry): Long

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournalEntry(id: Long)

    // Saved Mantras
    @Query("SELECT * FROM saved_mantras ORDER BY dateSaved DESC")
    fun getAllSavedMantras(): Flow<List<SavedMantra>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMantra(mantra: SavedMantra): Long

    @Query("DELETE FROM saved_mantras WHERE id = :id")
    suspend fun deleteMantra(id: Long)
}
