package fr.isen.straudo.smartcompanion2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.straudo.smartcompanion2.data.Database2
import fr.isen.straudo.smartcompanion2.ui.screens.AgendaScreen
import fr.isen.straudo.smartcompanion2.ui.screens.EventsScreen
import fr.isen.straudo.smartcompanion2.ui.screens.HistoryScreen
import fr.isen.straudo.smartcompanion2.ui.screens.MainScreen


sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Accueil")
    data object Events : Screen("events", "Événements")
    data object History : Screen("history", "Historique")
    data object Agenda : Screen("agenda", "Agenda")
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
                        Screen.Agenda -> Icon(Icons.Default.DateRange, contentDescription = screen.title)
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

@Composable
fun NavigationGraph(navController: NavHostController, db: fr.isen.straudo.smartcompanion2.Database2) {
    val context = LocalContext.current
    val db = Database2.getDatabase(context = context)

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainScreen() }
        composable(Screen.Events.route) { EventsScreen() }
        composable(Screen.History.route) { HistoryScreen(db = db) }
        composable(Screen.Agenda.route) {
            val viewModel: AgendaViewModel = viewModel()
            AgendaScreen(viewModel)
        }

    }
}