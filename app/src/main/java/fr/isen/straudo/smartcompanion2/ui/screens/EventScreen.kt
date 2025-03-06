package fr.isen.straudo.smartcompanion2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.straudo.smartcompanion2.Event
import fr.isen.straudo.smartcompanion2.EventItem
import fr.isen.straudo.smartcompanion2.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EventsScreen() {
    var eventsList by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // CoroutineScope for launching async tasks
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Utilisation du scope pour récupérer les événements
        scope.launch {
            try {
                // Appel à l'API de manière asynchrone
                val eventsResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getEventList() // Appel API
                }
                // Mise à jour de la liste d'événements
                eventsList = eventsResponse
            } catch (e: Exception) {
                // Gestion des erreurs
                errorMessage = "Erreur lors de la récupération des événements: ${e.localizedMessage}"
            } finally {
                // Fin du chargement
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Affichage du titre
        Text(
            text = "📅 Événements ISEN",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Si l'application charge les événements
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else if (errorMessage != null) {
            // Affichage des erreurs si elles surviennent
            Text(
                text = errorMessage ?: "Erreur inconnue",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            // Affichage des événements une fois récupérés
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(eventsList) { event ->
                    EventItem(event, context)

                    // Espacement et séparateur visuel
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
    }
}