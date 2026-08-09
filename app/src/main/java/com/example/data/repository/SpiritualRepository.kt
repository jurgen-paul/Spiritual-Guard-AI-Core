package com.example.data.repository

import com.example.data.api.GeminiService
import com.example.data.db.JournalEntry
import com.example.data.db.MeditationSession
import com.example.data.db.SavedMantra
import com.example.data.db.SpiritualDao
import kotlinx.coroutines.flow.Flow

class SpiritualRepository(private val dao: SpiritualDao) {

    val allSessions: Flow<List<MeditationSession>> = dao.getAllSessions()
    val allJournalEntries: Flow<List<JournalEntry>> = dao.getAllJournalEntries()
    val allSavedMantras: Flow<List<SavedMantra>> = dao.getAllSavedMantras()
    val sessionCount: Flow<Int> = dao.getSessionCount()

    suspend fun saveMeditationSession(session: MeditationSession): Long {
        return dao.insertSession(session)
    }

    suspend fun saveJournalEntry(entry: JournalEntry): Long {
        return dao.insertJournalEntry(entry)
    }

    suspend fun deleteJournalEntry(id: Long) {
        dao.deleteJournalEntry(id)
    }

    suspend fun saveMantra(mantra: SavedMantra): Long {
        return dao.insertMantra(mantra)
    }

    suspend fun deleteMantra(id: Long) {
        dao.deleteMantra(id)
    }

    // AI Integrations
    suspend fun generateMeditationScript(
        intent: String,
        mood: String,
        durationMinutes: Int
    ): String {
        val systemInstruction = """
            You are Spiritual Guard AI, a compassionate, peaceful, and wise spiritual guide.
            Your goal is to provide deeply restorative, personalized guided meditation scripts.
            Structure the response clearly with:
            1. Title and Energy Shield Purpose
            2. Phase 1: Inward Breath & Grounding
            3. Phase 2: Core Guided Visualization & Spiritual Protection
            4. Phase 3: Affirmation & Closing Energy Seal
            Keep tone serene, sacred, and comforting.
        """.trimIndent()

        val prompt = "Create a $durationMinutes-minute guided meditation script for someone whose current state is '$mood' and seeking '$intent'. Provide a comforting, serene, step-by-step experience."
        return GeminiService.generateContent(prompt, systemInstruction)
    }

    suspend fun generateSpiritualInsight(
        userQuery: String,
        currentMood: String
    ): String {
        val systemInstruction = """
            You are Spiritual Guard AI, offering sacred insights, spiritual reflection, emotional protection guidance, and aura shielding advice.
            Provide compassionate wisdom, actionable spiritual exercises, and a protective affirmation.
        """.trimIndent()

        val prompt = "The seeker shares: '$userQuery'. Their current emotion/state is '$currentMood'. Provide spiritual insight, loving perspective, and a protective mantra."
        return GeminiService.generateContent(prompt, systemInstruction)
    }

    suspend fun generateDailyMantra(focusTopic: String): Pair<String, String> {
        val systemInstruction = "You are Spiritual Guard AI. Generate a short, powerful sacred protection mantra and a brief 1-sentence explanation of its deeper spiritual power."
        val prompt = "Generate a daily mantra and meaning focusing on '$focusTopic'."
        val response = GeminiService.generateContent(prompt, systemInstruction)
        
        // Parse lines
        val lines = response.lines().filter { it.isNotBlank() }
        val mantra = lines.firstOrNull()?.replace("*", "")?.trim() ?: "I am centered, protected, and grounded in cosmic tranquility."
        val meaning = if (lines.size > 1) lines.drop(1).joinToString(" ").replace("*", "").trim() else "This mantra strengthens your inner shield against chaos and doubt."
        return Pair(mantra, meaning)
    }
}
