package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineFallback(prompt)
        }

        val requestJson = JSONObject()
        
        // System instruction
        if (!systemInstruction.isNullOrBlank()) {
            val systemObj = JSONObject()
            val systemParts = JSONArray()
            val sysPartObj = JSONObject()
            sysPartObj.put("text", systemInstruction)
            systemParts.put(sysPartObj)
            systemObj.put("parts", systemParts)
            requestJson.put("systemInstruction", systemObj)
        }

        // Contents
        val contentsArray = JSONArray()
        val contentObj = JSONObject()
        val partsArray = JSONArray()
        val textPart = JSONObject()
        textPart.put("text", prompt)
        partsArray.put(textPart)
        contentObj.put("parts", partsArray)
        contentsArray.put(contentObj)
        requestJson.put("contents", contentsArray)

        // Generation Config
        val configObj = JSONObject()
        configObj.put("temperature", 0.7)
        requestJson.put("generationConfig", configObj)

        val url = "$BASE_URL?key=$apiKey"
        val requestBody = requestJson.toString().toRequestBody(JSON_MEDIA_TYPE)

        val httpRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    return@withContext getOfflineFallback(prompt, "API error ${response.code}: $errBody")
                }

                val bodyString = response.body?.string() ?: return@withContext getOfflineFallback(prompt)
                val responseJson = JSONObject(bodyString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", getOfflineFallback(prompt))
                    }
                }
                return@withContext getOfflineFallback(prompt)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext getOfflineFallback(prompt, "Connection error: ${e.localizedMessage}")
        }
    }

    private fun getOfflineFallback(prompt: String, errorDetail: String? = null): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("meditation") || lower.contains("guided") -> {
                """
                |✨ **Spiritual Guard Guided Practice** ✨
                |
                |**1. Opening Breath (1 Minute)**
                |Find a quiet, grounded posture. Close your eyes softly. Take a deep inhale through your nose filling your chest with golden light, and exhale slowly releasing tension.
                |
                |**2. Centering Aura (2 Minutes)**
                |Visualize an impenetrable radiant shield surrounding your mind and heart. As thoughts arise, allow them to drift past like autumn leaves in a quiet stream. You are safe, present, and calm.
                |
                |**3. Protection Affirmation (1 Minute)**
                |Repeat internally: *"I am anchored in inner peace. No external storm can shake my quiet strength."*
                |
                |**4. Closing Seal**
                |Gently return your awareness to your body. Wiggle your fingers, feel the ground beneath you, and open your eyes with renewed calm.
                """.trimMargin()
            }
            lower.contains("insight") || lower.contains("oracle") || lower.contains("reflection") -> {
                """
                |🕊️ **Spiritual Guard Insight**
                |
                |*Wisdom for your Heart:*
                |"Peace is not the absence of external noise, but the quiet presence of your inner sanctuary."
                |
                |**Spiritual Guard Guidance:**
                |When faced with uncertainty or overwhelm, protect your energetic boundary. Take three conscious, deep breaths before reacting. Recognize that you hold the key to your own tranquility.
                |
                |**Daily Sacred Mantra:**
                |*"My inner energy is guarded by divine clarity and gentle strength."*
                """.trimMargin()
            }
            else -> {
                """
                |🔮 **Spiritual Guard Reflection**
                |
                |May golden energy surround your thoughts today. Honor your need for quiet moments, breathe deeply, and carry an unwavering sense of inner peace wherever you go.
                """.trimMargin()
            }
        }
    }
}
