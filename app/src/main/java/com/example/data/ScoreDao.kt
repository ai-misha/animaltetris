package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM high_scores WHERE difficulty = :difficulty ORDER BY score DESC, linesCleared DESC, timestamp DESC LIMIT 25")
    fun getTopScoresByDifficulty(difficulty: String): Flow<List<ScoreRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreRecord: ScoreRecord): Long

    @Query("DELETE FROM high_scores WHERE difficulty = :difficulty")
    suspend fun clearScoresForDifficulty(difficulty: String)

    @Query("DELETE FROM high_scores")
    suspend fun clearAllScores()
}
