package fr.isen.straudo.smartcompanion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import fr.isen.straudo.smartcompanion2.data.Database2
import java.text.SimpleDateFormat
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(db: Database2) {
    val dao = db.questionAnswerDao()
    val history by dao.getAllQuestionAnswers().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historique des questions",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE0EDC6)
                ),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch { dao.deleteAll() }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete All", tint = Color.Black)
                    }
                }
            )
        },

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
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Question:",
                                        color = Color(0xFF1565C0), // Bleu foncé
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = item.question,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Réponse:",
                                        color = Color(0xFFD32F2F), // Rouge foncé
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = item.answer,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.DarkGray
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "🕒 ${formatDate(item.timestamp)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Button(
                                        onClick = {
                                            coroutineScope.launch { dao.deleteQuestionAnswer(item) }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Text("Supprimer", color = Color.White)
                                    }
                                }
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
