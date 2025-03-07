package fr.isen.straudo.smartcompanion2.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.straudo.smartcompanion2.AssistantActivity
import fr.isen.straudo.smartcompanion2.R

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE)) // Rouge très clair pour le fond
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Bienvenue !",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFD50000), // Rouge vif pour le titre
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(context, AssistantActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD50000), // Rouge vif
                    contentColor = Color.White // Texte blanc
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Accéder à l'Assistant", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
