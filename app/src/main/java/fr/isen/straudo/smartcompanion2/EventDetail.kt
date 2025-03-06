package fr.isen.straudo.smartcompanion2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.straudo.smartcompanion2.data.AgendaItem
import fr.isen.straudo.smartcompanion2.data.NotificationHelper.scheduleNotification
import fr.isen.straudo.smartcompanion2.data.PreferencesManager
import fr.isen.straudo.smartcompanion2.ui.theme.SmartCompanion2Theme
import kotlinx.coroutines.launch


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartCompanion2Theme {
                // Récupérer l'objet Event à partir de l'intention
                val event = intent.getSerializableExtra("event") as? Event
                val viewModel: EventViewModel by viewModels()
                val agendaViewModel: AgendaViewModel by viewModels()

                if (event != null) {
                    EventDetailScreen(event, viewModel, agendaViewModel)  // Passer l'Activity ici
                } else {
                    Text("Aucun événement sélectionné")
                }
            }
        }
    }
}

@Composable
fun EventDetailScreen(event: Event, viewModel: EventViewModel, agendaViewModel: AgendaViewModel) {
    val context = LocalContext.current
    val isNotified by viewModel.isEventNotified(event.id).collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    val preferencesManager = remember { PreferencesManager(context) }
    var isNotifiedLocal by remember { mutableStateOf(isNotified) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        IconButton(
            onClick = {
                        // Retourner à l'écran d'accueil
                        context.startActivity(Intent(context, MainActivity::class.java))
                        // Finir l'activité actuelle
                        (context as? Activity)?.finish()
                    },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Utilisation de l'icône de flèche de Material Icons
                contentDescription = "Retour", // Description de l'icône pour l'accessibilité
                tint = Color.Black
            )
        }
        // Contenu principal centré
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow("📅 Date", event.date)
                    InfoRow("📍 Lieu", event.location)
                    InfoRow("🏷 Catégorie", event.category)
                }
            }
        }

        // Bouton pour activer la notification
        Button(
            onClick = {
                isNotifiedLocal = !isNotifiedLocal
                scope.launch {
                    preferencesManager.setNotificationStatus(event.id, isNotifiedLocal)
                    if (isNotifiedLocal) {
                        scheduleNotification(event.id, event.title, context)

                        val agendaItem = AgendaItem(
                            id = event.id,
                            title = event.title,
                            description = event.description, // Ajouter description ici
                            date = event.date,
                            location = event.location,
                            category = event.category
                        )

                        Log.d("EventDetail", "Ajout de l'événement à l'agenda : ${agendaItem.title}")
                        agendaViewModel.addAgendaItem(agendaItem)

                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Activer notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isNotifiedLocal) "Notification activée" else "Activer la notification")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, color = Color.DarkGray, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailScreenPreview() {
    SmartCompanion2Theme {
        // Ajouter un prévisualisation ici
    }
}
