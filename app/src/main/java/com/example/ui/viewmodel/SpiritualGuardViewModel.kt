package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.JournalEntry
import com.example.data.db.MeditationSession
import com.example.data.db.SavedMantra
import com.example.data.repository.SpiritualRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class BreathingPhase(val label: String, val seconds: Int) {
    INHALE("Inhale Light...", 4),
    HOLD("Hold Energy...", 4),
    EXHALE("Exhale Tension...", 4),
    REST("Rest & Ground...", 2)
}

enum class MeditationStatus {
    IDLE, GENERATING, READY, PLAYING, PAUSED, FINISHED
}

class SpiritualGuardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SpiritualRepository = SpiritualRepository(AppDatabase.getInstance(application).spiritualDao())

    // Repository Flows
    val sessions = repository.allSessions
    val journalEntries = repository.allJournalEntries
    val savedMantras = repository.allSavedMantras

    init {
        loadInitialData()
        observeSessions()
    }


    // Current Active Tab (0 = Sanctuary, 1 = Meditation, 2 = Oracle, 3 = Journal)
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Sanctuary State
    private val _dailyMantra = MutableStateFlow("I am surrounded by an impenetrable shield of golden light, peace, and clarity.")
    val dailyMantra: StateFlow<String> = _dailyMantra.asStateFlow()

    private val _dailyMantraMeaning = MutableStateFlow("This ancient intention guards your energy against external turbulence and anchors your heart in calm.")
    val dailyMantraMeaning: StateFlow<String> = _dailyMantraMeaning.asStateFlow()

    private val _shieldEnergy = MutableStateFlow(78)
    val shieldEnergy: StateFlow<Int> = _shieldEnergy.asStateFlow()

    private val _streakDays = MutableStateFlow(5)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _isMantraLoading = MutableStateFlow(false)
    val isMantraLoading: StateFlow<Boolean> = _isMantraLoading.asStateFlow()

    fun refreshDailyMantra(topic: String = "Inner Strength & Peace") {
        viewModelScope.launch {
            _isMantraLoading.value = true
            try {
                val (mantra, meaning) = repository.generateDailyMantra(topic)
                _dailyMantra.value = mantra
                _dailyMantraMeaning.value = meaning
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isMantraLoading.value = false
            }
        }
    }

    fun saveCurrentMantra() {
        viewModelScope.launch {
            repository.saveMantra(
                SavedMantra(
                    mantraText = _dailyMantra.value,
                    meaningText = _dailyMantraMeaning.value,
                    category = "Daily Protection"
                )
            )
        }
    }

    // Meditation Generator State
    private val _selectedMood = MutableStateFlow("Anxiety & Overwhelm")
    val selectedMood: StateFlow<String> = _selectedMood.asStateFlow()

    private val _selectedIntent = MutableStateFlow("Protection Energy Shield")
    val selectedIntent: StateFlow<String> = _selectedIntent.asStateFlow()

    private val _selectedDuration = MutableStateFlow(5) // Minutes
    val selectedDuration: StateFlow<Int> = _selectedDuration.asStateFlow()

    private val _meditationStatus = MutableStateFlow(MeditationStatus.IDLE)
    val meditationStatus: StateFlow<MeditationStatus> = _meditationStatus.asStateFlow()

    private val _generatedScript = MutableStateFlow("")
    val generatedScript: StateFlow<String> = _generatedScript.asStateFlow()

    // Active Timer & Breathing
    private val _remainingSeconds = MutableStateFlow(300)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _currentBreathingPhase = MutableStateFlow(BreathingPhase.INHALE)
    val currentBreathingPhase: StateFlow<BreathingPhase> = _currentBreathingPhase.asStateFlow()

    private var timerJob: Job? = null
    private var breathingJob: Job? = null

    fun setMood(mood: String) { _selectedMood.value = mood }
    fun setIntent(intent: String) { _selectedIntent.value = intent }
    fun setDuration(minutes: Int) {
        _selectedDuration.value = minutes
        _remainingSeconds.value = minutes * 60
    }

    fun generateMeditation() {
        viewModelScope.launch {
            _meditationStatus.value = MeditationStatus.GENERATING
            try {
                val script = repository.generateMeditationScript(
                    intent = _selectedIntent.value,
                    mood = _selectedMood.value,
                    durationMinutes = _selectedDuration.value
                )
                _generatedScript.value = script
                _remainingSeconds.value = _selectedDuration.value * 60
                _meditationStatus.value = MeditationStatus.READY
            } catch (e: Exception) {
                e.printStackTrace()
                _meditationStatus.value = MeditationStatus.IDLE
            }
        }
    }

    fun startMeditationSession() {
        _meditationStatus.value = MeditationStatus.PLAYING
        startTimer()
        startBreathingCycle()
    }

    fun pauseMeditationSession() {
        _meditationStatus.value = MeditationStatus.PAUSED
        timerJob?.cancel()
        breathingJob?.cancel()
    }

    fun resumeMeditationSession() {
        _meditationStatus.value = MeditationStatus.PLAYING
        startTimer()
        startBreathingCycle()
    }

    fun stopMeditationSession() {
        timerJob?.cancel()
        breathingJob?.cancel()
        _meditationStatus.value = MeditationStatus.IDLE
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _meditationStatus.value == MeditationStatus.PLAYING) {
                delay(1000L)
                _remainingSeconds.value -= 1
            }
            if (_remainingSeconds.value <= 0) {
                completeMeditationSession()
            }
        }
    }

    private fun startBreathingCycle() {
        breathingJob?.cancel()
        breathingJob = viewModelScope.launch {
            val phases = BreathingPhase.entries
            var phaseIndex = 0
            while (_meditationStatus.value == MeditationStatus.PLAYING) {
                val current = phases[phaseIndex]
                _currentBreathingPhase.value = current
                delay(current.seconds * 1000L)
                phaseIndex = (phaseIndex + 1) % phases.size
            }
        }
    }

    private fun completeMeditationSession() {
        _meditationStatus.value = MeditationStatus.FINISHED
        timerJob?.cancel()
        breathingJob?.cancel()

        viewModelScope.launch {
            // Save completed session to Room
            repository.saveMeditationSession(
                MeditationSession(
                    title = "${_selectedIntent.value} Guidance",
                    intent = _selectedIntent.value,
                    durationMinutes = _selectedDuration.value,
                    summary = _generatedScript.value.take(200) + "...",
                    moodBefore = _selectedMood.value,
                    moodAfter = "Restored & Peaceful",
                    shieldEnergyGained = 20
                )
            )
            // Increase shield energy and streak
            _shieldEnergy.value = (_shieldEnergy.value + 15).coerceAtMost(100)
        }
    }

    // Oracle / Spiritual Insight State
    private val _oracleQuery = MutableStateFlow("")
    val oracleQuery: StateFlow<String> = _oracleQuery.asStateFlow()

    private val _oracleResult = MutableStateFlow("")
    val oracleResult: StateFlow<String> = _oracleResult.asStateFlow()

    private val _isOracleLoading = MutableStateFlow(false)
    val isOracleLoading: StateFlow<Boolean> = _isOracleLoading.asStateFlow()

    fun setOracleQuery(text: String) { _oracleQuery.value = text }

    fun askOracle() {
        if (_oracleQuery.value.isBlank()) return
        viewModelScope.launch {
            _isOracleLoading.value = true
            try {
                val insight = repository.generateSpiritualInsight(
                    userQuery = _oracleQuery.value,
                    currentMood = _selectedMood.value
                )
                _oracleResult.value = insight
                
                // Auto save insight to Journal
                repository.saveJournalEntry(
                    JournalEntry(
                        userReflection = _oracleQuery.value,
                        aiGuidanceText = insight,
                        moodCategory = _selectedMood.value
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isOracleLoading.value = false
            }
        }
    }

    // Journal Operations
    fun deleteJournalEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
        }
    }

    fun deleteSavedMantra(id: Long) {
        viewModelScope.launch {
            repository.deleteMantra(id)
        }
    }

    private fun loadInitialData() {
        // Preset default values
    }

    private fun observeSessions() {
        viewModelScope.launch {
            sessions.collectLatest { list ->
                if (list.isNotEmpty()) {
                    _streakDays.value = (list.size + 3).coerceAtMost(30)
                }
            }
        }
    }
}
