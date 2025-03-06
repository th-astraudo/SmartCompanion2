package fr.isen.straudo.smartcompanion2


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.straudo.smartcompanion2.data.PreferencesManager
import androidx.compose.runtime.rememberCoroutineScope
import fr.isen.straudo.smartcompanion2.ui.screens.EventsScreen
import fr.isen.straudo.smartcompanion2.ui.theme.SmartCompanion2Theme
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.isen.straudo.smartcompanion2.data.NotificationHelper.scheduleNotification


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCompanion2Theme {
                // Récupérer l'objet Event à partir de l'intention
                val event = intent.getSerializableExtra("event") as? Event
                val viewModel: EventViewModel by viewModels()
                if (event != null) {
                    EventDetailScreen(event, viewModel)
                } else {
                    Text("Aucun événement sélectionné")
                }


            }
        }
    }
}

@Composable
fun EventDetailScreen(event: Event, viewModel: EventViewModel) {
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
        // Icône de retour
        IconButton(
            onClick = {
                context.startActivity(Intent(context, Event::class.java))
                (context as? Activity)?.finish()
            },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
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
                    Divider(color = Color.Red, thickness = 1.dp)
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
                        scheduleNotification(event.id.toString(), event.title, context)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
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

    }
}