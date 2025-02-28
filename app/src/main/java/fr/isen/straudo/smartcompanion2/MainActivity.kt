package fr.isen.straudo.smartcompanion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.straudo.smartcompanion2.ui.theme.SmartCompanion2Theme
import fr.isen.straudo.smartcompanion2.navigation.MainApp


import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCompanion2Theme {
                MainApp() // L'application démarre avec MainApp()
            }
        }
    }
}

interface ApiService {
    @GET("events.json")
    suspend fun getEventList(): List<Event>
}