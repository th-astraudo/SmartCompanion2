package fr.isen.straudo.smartcompanion2.data

// AgendaItem.kt
import java.io.Serializable

data class AgendaItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable


