package com.example.data

import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {
    fun getTopScores(difficulty: String): Flow<List<ScoreRecord>> =
        scoreDao.getTopScoresByDifficulty(difficulty)

    suspend fun saveScore(record: ScoreRecord): Long =
        scoreDao.insertScore(record)

    suspend fun clearScores(difficulty: String) =
        scoreDao.clearScoresForDifficulty(difficulty)

    suspend fun clearAll() =
        scoreDao.clearAllScores()
}
