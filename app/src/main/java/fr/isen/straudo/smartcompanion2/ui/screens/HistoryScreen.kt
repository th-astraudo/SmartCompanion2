package fr.isen.straudo.smartcompanion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import fr.isen.straudo.smartcompanion2.data.Database2
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(db: Database2) {
    val dao = db.questionAnswerDao()
    val history by dao.getAllQuestionAnswers().collectAsState(initial = emptyList()) // Utilisation correcte du Flow

    Scaffold(
        topBar = { TopAppBar(title = { Text("Historique des activités") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (history.isEmpty()) {
                    Text("Aucune activité trouvée.")
                } else {
                    LazyColumn {
                        items(history) { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text("Question: ${item.question}")
                                Text("Réponse: ${item.answer}")
                                Text("Date: ${formatDate(item.timestamp)}")
                                Divider(color = Color.Gray, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    )
}

// Fonction pour formater le timestamp en date lisible
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
