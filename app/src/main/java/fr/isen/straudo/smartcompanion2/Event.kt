package fr.isen.straudo.smartcompanion2
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Date
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

// Définition du modèle d'événement
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable

fun generateFakeEvents(): List<Event> {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val events = mutableListOf<Event>()

    events.add(
        Event(
            id = "a",
            title = "Soirée BDE",
            description = "Une soirée inoubliable organisée par le BDE.",
            date = "2025-02-27",
            location = "Kudeta",
            category = "Soirée"
        )
    )

    events.add(
        Event(
            id = "b",
            title = "Gala ISEN",
            description = "Le gala annuel avec dîner et animations.",
            date = "2025-03-21",
            location = "Chateau",
            category = "Gala"
        )
    )

    events.add(
        Event(
            id = "c",
            title = "Journée de Cohésion",
            description = "Une journée pour renforcer l’esprit d’équipe.",
            date = "2024-09-10",
            location = "Plage du Mourillon",
            category = "Cohésion"
        )
    )

    events.add(
        Event(
            id = "d",
            title = "Soirée du jeudi",
            description = "Une soirée inoubliable au Kudeta.",
            date = "2025-02-28",
            location = "Kudeta Bar",
            category = "Soirée"
        )
    )

    events.add(
        Event(
            id = "e",
            title = "Afterwork",
            description = "Venez vous détendre après les cours.",
            date = "2025-03-03",
            location = "Café de la paix",
            category = "Afterwork"
        )
    )

    return events
}

@Composable
fun EventItem(event: Event, context: Context) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrage des éléments
        ) {
            // Titre
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton centré
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, EventDetailActivity::class.java).apply {
                            putExtra("event", event)
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Détails")
                }
            }
        }
    }
}