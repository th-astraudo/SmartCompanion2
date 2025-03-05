package fr.isen.straudo.smartcompanion2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.isen.straudo.smartcompanion2.AgendaViewModel
import androidx.compose.ui.Modifier

@Composable
fun AgendaScreen(viewModel: AgendaViewModel) {
    val agendaItems by viewModel.agendaItems.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mon Agenda", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
            items(agendaItems.size) { index ->
                val item = agendaItems[index]
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
