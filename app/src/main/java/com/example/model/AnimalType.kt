package com.example.model

import androidx.compose.ui.graphics.Color

enum class AnimalType(
    val ruName: String,
    val emoji: String,
    val primaryColor: Color,
    val highlightColor: Color,
    val darkShadowColor: Color,
    val faceFeatureColor: Color
) {
    FROG(
        ruName = "Лягушонок",
        emoji = "🐸",
        primaryColor = Color(0xFF4CAF50),
        highlightColor = Color(0xFF81C784),
        darkShadowColor = Color(0xFF2E7D32),
        faceFeatureColor = Color(0xFF1B5E20)
    ),
    CAT(
        ruName = "Котёнок",
        emoji = "🐱",
        primaryColor = Color(0xFFFFB300),
        highlightColor = Color(0xFFFFD54F),
        darkShadowColor = Color(0xFFFF8F00),
        faceFeatureColor = Color(0xFFE65100)
    ),
    PIGGY(
        ruName = "Хрюшка",
        emoji = "🐷",
        primaryColor = Color(0xFFF06292),
        highlightColor = Color(0xFFF8BBD0),
        darkShadowColor = Color(0xFFD81B60),
        faceFeatureColor = Color(0xFFAD1457)
    ),
    PANDA(
        ruName = "Пандочка",
        emoji = "🐼",
        primaryColor = Color(0xFF26A69A),
        highlightColor = Color(0xFF80CBC4),
        darkShadowColor = Color(0xFF00796B),
        faceFeatureColor = Color(0xFF004D40)
    ),
    BEAR(
        ruName = "Мишутка",
        emoji = "🐻",
        primaryColor = Color(0xFF8D6E63),
        highlightColor = Color(0xFFBCAAA4),
        darkShadowColor = Color(0xFF5D4037),
        faceFeatureColor = Color(0xFF3E2723)
    ),
    BUNNY(
        ruName = "Зайка",
        emoji = "🐰",
        primaryColor = Color(0xFF7E57C2),
        highlightColor = Color(0xFFB39DDB),
        darkShadowColor = Color(0xFF512DA8),
        faceFeatureColor = Color(0xFF311B92)
    ),
    FOX(
        ruName = "Лисичка",
        emoji = "🦊",
        primaryColor = Color(0xFFFF7043),
        highlightColor = Color(0xFFFFAB91),
        darkShadowColor = Color(0xFFE64A19),
        faceFeatureColor = Color(0xFFBF360C)
    )
}
