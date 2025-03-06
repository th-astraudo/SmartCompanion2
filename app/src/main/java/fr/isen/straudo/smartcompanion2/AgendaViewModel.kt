package fr.isen.straudo.smartcompanion2


import android.util.Log
import androidx.lifecycle.ViewModel
import fr.isen.straudo.smartcompanion2.data.AgendaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AgendaViewModel : ViewModel() {
    private val _agendaItems = MutableStateFlow<List<AgendaItem>>(emptyList())
    val agendaItems: StateFlow<List<AgendaItem>> = _agendaItems

    init {
        _agendaItems.value = generateFakeAgenda()
    }

    private fun generateFakeAgenda(): List<AgendaItem> {
        return listOf(
            AgendaItem(id = "1", title = "Réunion projet ISEN", date = "2025-03-06", location = "Salle 101", description ="", category = ""),
            AgendaItem(id = "2", title = "Déjeuner avec le BDE", date = "2025-03-07", location = "Cafétéria", description ="", category = ""),
            AgendaItem(id = "3", title = "Réunion de préparation Hackathon", date = "2025-03-09", location = "Salle 202", description ="", category = "")
        )
    }

    fun addAgendaItem(newEvent: AgendaItem) {
        val updatedAgenda = _agendaItems.value.toMutableList().apply {
            add(newEvent)
        }
        _agendaItems.value = updatedAgenda
        Log.d("AgendaViewModel", "Événement ajouté : ${newEvent.title}")
    }
}

