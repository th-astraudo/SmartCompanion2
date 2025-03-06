package fr.isen.straudo.smartcompanion2.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.isen.straudo.smartcompanion2.AgendaViewModel

@Composable
fun AgendaScreen(viewModel: AgendaViewModel) {
    val agendaItems by viewModel.agendaItems.collectAsStateWithLifecycle()
    Log.d("AgendaScreen", "Nombre d'éléments dans l'agenda : ${agendaItems.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "📅 Mon Agenda",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Red,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            agendaItems.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)) // Rouge clair
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("📅 Date: ${item.date}", style = MaterialTheme.typography.bodyMedium)
                        Text("📍 Lieu: ${item.location}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

    }
}