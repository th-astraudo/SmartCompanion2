package fr.isen.straudo.smartcompanion2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionAnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionAnswer(questionAnswer: QuestionAnswer)

    @Query("SELECT * FROM question_answer_table ORDER BY timestamp DESC")
    fun getAllQuestionAnswers(): kotlinx.coroutines.flow.Flow<List<QuestionAnswer>>

    @Delete
    suspend fun deleteQuestionAnswer(questionAnswer: QuestionAnswer)

    @Query("DELETE FROM question_answer_table")
    suspend fun deleteAll()
}
