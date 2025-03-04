package fr.isen.straudo.smartcompanion2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestionAnswer::class], version = 1, exportSchema = false)
abstract class Database2 : RoomDatabase() {
    abstract fun questionAnswerDao(): QuestionAnswerDAO

    companion object {
        @Volatile
        private var INSTANCE: Database2? = null

        fun getDatabase(context: Context): Database2 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database2::class.java,
                    "question_answer_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
