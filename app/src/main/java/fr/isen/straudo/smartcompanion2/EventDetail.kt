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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        IconButton(
            onClick = {
                // Retourner à l'écran d'accueil
                context.startActivity(Intent(context, Event::class.java))
                // Finir l'activité actuelle
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = Color.Black
            )
        }
        // Vérifier si l'événement est null
        if (event != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Date: ${event.date}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Lieu: ${event.location}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Catégorie: ${event.category}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                IconButton(onClick = {
                    isNotifiedLocal = !isNotifiedLocal
                    scope.launch {
                        preferencesManager.setNotificationStatus(event.id, isNotifiedLocal) // Active la notification
                        if (isNotifiedLocal) {
                            scheduleNotification(event.id.toString(), event.title, context) // Programme la notification si activée
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Notifications,
                        contentDescription = "Activer notifications",
                        tint = if (isNotifiedLocal) Color.Green else Color.Gray )
                }
            }
        } else {
            Text("Aucun événement sélectionné")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EventDetailScreenPreview() {
    SmartCompanion2Theme {

    }
}