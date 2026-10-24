package com.example.model

enum class Difficulty(
    val titleRu: String,
    val initialSpeedMs: Long,
    val minSpeedMs: Long,
    val speedStepMs: Long,
    val scoreMultiplier: Float,
    val badgeIcon: String,
    val descriptionRu: String
) {
    EASY(
        titleRu = "Лёгкий",
        initialSpeedMs = 850L,
        minSpeedMs = 380L,
        speedStepMs = 30L,
        scoreMultiplier = 1.0f,
        badgeIcon = "🌱",
        descriptionRu = "Спокойный темп для отдыха и новичков"
    ),
    MEDIUM(
        titleRu = "Средний",
        initialSpeedMs = 600L,
        minSpeedMs = 220L,
        speedStepMs = 38L,
        scoreMultiplier = 1.5f,
        badgeIcon = "⭐",
        descriptionRu = "Классический баланс скорости и очков"
    ),
    HARD(
        titleRu = "Сложный",
        initialSpeedMs = 380L,
        minSpeedMs = 120L,
        speedStepMs = 45L,
        scoreMultiplier = 2.5f,
        badgeIcon = "🔥",
        descriptionRu = "Высокий темп для настоящих профи"
    );

    fun getSpeedForLevel(level: Int): Long {
        val calculated = initialSpeedMs - (level - 1) * speedStepMs
        return calculated.coerceAtLeast(minSpeedMs)
    }
}
