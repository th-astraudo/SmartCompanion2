package fr.isen.straudo.smartcompanion2


import androidx.lifecycle.ViewModel
import fr.isen.straudo.smartcompanion2.data.AgendaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AgendaViewModel : ViewModel() {
    private val _agendaItems = MutableStateFlow(generateFakeAgenda())
    val agendaItems: StateFlow<List<AgendaItem>> = _agendaItems

    private fun generateFakeAgenda(): List<AgendaItem> {
        return listOf(
            AgendaItem(id = "1", title = "Réunion projet ISEN", date = "2025-03-06", location = "Salle 101"),
            AgendaItem(id = "2", title = "Déjeuner avec le BDE", date = "2025-03-07", location = "Cafétéria"),
            AgendaItem(id = "3", title = "Séminaire sur l'IA", date = "2025-03-08", location = "Amphithéâtre B"),
            AgendaItem(id = "4", title = "Réunion de préparation Hackathon", date = "2025-03-09", location = "Salle 202")
        )
    }
}

