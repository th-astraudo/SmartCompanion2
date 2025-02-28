package fr.isen.straudo.smartcompanion2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.straudo.smartcompanion2.ui.theme.SmartCompanion2Theme
import kotlinx.coroutines.launch


class AssistantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCompanion2Theme {
                AssistantScreen()  // Appeler votre AssistantScreen ici
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen() {
    var question by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("Posez votre question") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Conteneur du logo et du titre
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Smart Companion",
                    color = Color.Blue,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        // Conteneur pour le champ de texte et le bouton
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Posez votre question ici") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        //response = fakeAIResponse(question)
                        if (question.isNotEmpty()) {
                            coroutineScope.launch {
                                response = "Analyse en cours..."
                                response = GeminiAI.analyzeText(question)
                            }
                        } else {
                            Toast.makeText(context, "Veuillez entrer une question", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Envoyer")
                }
            }
        }

        // Icône de retour (flèche)
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
                imageVector = Icons.Filled.ArrowBack, // Utilisation de l'icône de flèche de Material Icons
                contentDescription = "Retour", // Description de l'icône pour l'accessibilité
                tint = Color.Black
            )
        }

        // Conteneur pour afficher la réponse
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = response,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Fonction qui génère une fausse réponse
//fun fakeAIResponse(question: String): String {
// return if (question.isNotBlank()) {
//    "Ceci est une réponse générée pour : $question"
// } else {
//      "Veuillez poser une question !"
//  }
//}

// Preview
@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    SmartCompanion2Theme {
        AssistantScreen()
    }
}