package fr.isen.straudo.smartcompanion2

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiAI {
    private val apiKey: String = BuildConfig.apiKey

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey// Ajuste directement la température ici si possible
    )

    suspend fun analyzeText(input: String): String {
        return withContext(Dispatchers.IO) {
            try {

                val response = model.generateContent(content { text(input)})
                response.text ?: "Pas de réponse générée"
            } catch (e: Exception) {
                "Erreur: ${e.message}"
            }
        }
    }
}