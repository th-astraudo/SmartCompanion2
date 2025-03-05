package fr.isen.straudo.smartcompanion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.isen.straudo.smartcompanion2.AgendaViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdgendaScreen(viewModel: AgendaViewModel) {
    val agendaItems by viewModel.agendaItems.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mon Agenda", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
            items(agendaItems) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.title, style = MaterialTheme.typography.bodyLarge)
                        Text("Date: ${item.date}", style = MaterialTheme.typography.bodyMedium)
                        Text("Lieu: ${item.location}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
