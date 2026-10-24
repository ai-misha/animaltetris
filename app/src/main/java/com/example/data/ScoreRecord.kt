package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class ScoreRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val difficulty: String, // "EASY", "MEDIUM", "HARD"
    val playerName: String,
    val score: Int,
    val linesCleared: Int,
    val level: Int,
    val timestamp: Long = System.currentTimeMillis()
)
