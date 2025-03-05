package fr.isen.straudo.smartcompanion2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.straudo.smartcompanion2.ui.screens.EventsScreen
import fr.isen.straudo.smartcompanion2.ui.screens.HistoryScreen
import fr.isen.straudo.smartcompanion2.ui.screens.MainScreen
import fr.isen.straudo.smartcompanion2.ui.screens.AdgendaScreen
import fr.isen.straudo.smartcompanion2.data.Database2
import androidx.compose.ui.platform.LocalContext


sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Accueil")
    object Events : Screen("events", "Événements")
    object History : Screen("history", "Historique")
    object Agenda : Screen("agenda", "Agenda")
}

@Composable
fun MainApp(db: fr.isen.straudo.smartcompanion2.Database2) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = androidx.compose.ui.Modifier.padding(paddingValues)) {
            NavigationGraph(navController, db)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(Screen.Home, Screen.Events, Screen.History, Screen.Agenda)

    NavigationBar(containerColor = Color.White) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        Screen.Home -> Icon(Icons.Default.Home, contentDescription = screen.title)
                        Screen.Events -> Icon(Icons.Default.Search, contentDescription = screen.title)
                        Screen.History -> Icon(Icons.Default.ArrowBack, contentDescription = screen.title)
                        Screen.Agenda -> Icon(Icons.Default.agenda, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) },
                selected = false,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(navController: NavHostController, db: fr.isen.straudo.smartcompanion2.Database2) {
    val context = LocalContext.current
    val db = Database2.getDatabase(context = context)

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainScreen() }
        composable(Screen.Events.route) { EventsScreen() }
        composable(Screen.History.route) { HistoryScreen(db = db) }
        composable(Screen.Agenda.route) { AdgendaScreen() }
    }
}