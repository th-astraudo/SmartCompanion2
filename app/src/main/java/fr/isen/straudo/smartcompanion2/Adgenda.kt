package fr.isen.straudo.smartcompanion2


import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AgendaViewModel : ViewModel() {
    private val _agendaItems = MutableLiveData<List<AgendaItem>>()
    val agendaItems: LiveData<List<AgendaItem>> get() = _agendaItems

    init {
        // Exemple de données : à remplacer par les événements réels
        _agendaItems.value = listOf(
            AgendaItem("Mathématiques 101", "6 mars 2025", "Salle 101"),
            AgendaItem("Conférence IA", "7 mars 2025", "Salle de conférence"),
            AgendaItem("Physique 201", "8 mars 2025", "Salle 102")
        )
    }
}
