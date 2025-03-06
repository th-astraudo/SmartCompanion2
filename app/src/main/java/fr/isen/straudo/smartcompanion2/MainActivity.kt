package fr.isen.straudo.smartcompanion2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.straudo.smartcompanion2.data.NotificationHelper
import fr.isen.straudo.smartcompanion2.ui.theme.SmartCompanion2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.GET


// 1. Définition de l'entité Room
@Entity(tableName = "question_answer_table")
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

// 2. DAO pour interagir avec la base de données
@Dao
interface QuestionAnswerDao {
    @Insert
    suspend fun insert(questionAnswer: QuestionAnswer)

    @Query("SELECT * FROM question_answer_table ORDER BY timestamp DESC")
    fun getAllQuestionAnswers(): List<QuestionAnswer>

    @Delete
    suspend fun delete(questionAnswer: QuestionAnswer)

    @Query("DELETE FROM question_answer_table")
    suspend fun deleteAll()
}

// 3. Création de la base de données Room
@Database(entities = [QuestionAnswer::class], version = 1, exportSchema = false)
abstract class Database2 : RoomDatabase() {
    abstract fun questionAnswerDao(): QuestionAnswerDao

    companion object {
        @Volatile
        private var INSTANCE: Database2? = null

        fun getDatabase(context: android.content.Context): Database2 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database2::class.java,
                    "question_answer_db"
                )
                    .fallbackToDestructiveMigration() // Évite les crashes lors de la mise à jour de la DB
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// 4. MainActivity avec enregistrement des interactions dans Room
class MainActivity : ComponentActivity() {
    private lateinit var database: Database2
    companion object {
        const val REQUEST_CODE_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_PERMISSION)
            }
        }

        // Initialisation de la base de données
        database = Database2.getDatabase(this)

        setContent {
            SmartCompanion2Theme {
                MainApp(database) // Passe la base de données à MainApp
            }
        }
    }

    // Fonction pour enregistrer une question/réponse dans la base de données
    private fun saveInteraction(question: String, answer: String) {
        val dao = database.questionAnswerDao()
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insert(QuestionAnswer(question = question, answer = answer))
        }
    }

    // Appelle cette fonction après la réponse de l'IA
    private fun onAIResponse(userQuestion: String, aiResponse: String) {
        saveInteraction(userQuestion, aiResponse)
    }
}

// 5. Interface Retrofit pour récupérer les événements
interface ApiService {
    @GET("events.json")
    suspend fun getEventList(): List<Event>
}
