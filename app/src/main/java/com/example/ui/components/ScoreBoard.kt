package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.GameState
import com.example.model.Piece
import com.example.ui.theme.LocalGameColors

@Composable
fun ScoreHeader(
    gameState: GameState,
    isMuted: Boolean,
    isDarkTheme: Boolean,
    onToggleMute: () -> Unit,
    onToggleTheme: () -> Unit,
    onTogglePause: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onSelectDifficulty: (Difficulty) -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalGameColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Top Bar: App Icon + Title & Level on left, Score Card on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Avatar & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Purple circular icon with pets
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (colors.isDark) colors.primaryContainer else Color(0xFF6750A4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = "Animal Tetris",
                        tint = if (colors.isDark) colors.onPrimaryContainer else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = "Animal Tetris",
                        color = colors.textPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "Уровень ${String.format("%02d", gameState.level)} • Линий: ${gameState.linesCleared}",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Sleek M3 Score Card
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.primaryContainer)
                    .border(
                        width = 1.dp,
                        color = if (colors.isDark) colors.outline.copy(alpha = 0.5f) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "СЧЁТ",
                        color = colors.onPrimaryContainer.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    AnimatedContent(
                        targetState = gameState.score,
                        transitionSpec = {
                            slideInVertically { -it } togetherWith slideOutVertically { it }
                        },
                        label = "score_anim"
                    ) { scoreVal ->
                        Text(
                            text = String.format("%,d", scoreVal),
                            color = colors.onPrimaryContainer,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Second Row: Difficulty Chips + Action Controls (Leaderboard, Sound, Theme, Pause, Restart)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Difficulty Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Difficulty.entries.forEach { diff ->
                    val isSelected = diff == gameState.difficulty
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) {
                                    if (colors.isDark) colors.primary else Color(0xFF6750A4)
                                } else colors.secondaryContainer
                            )
                            .then(
                                if (!isSelected) {
                                    Modifier.border(1.dp, colors.outline, RoundedCornerShape(16.dp))
                                } else Modifier
                            )
                            .clickable { onSelectDifficulty(diff) }
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                            .testTag("chip_${diff.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = diff.titleRu,
                            color = if (isSelected) {
                                if (colors.isDark) colors.onPrimary else Color.White
                            } else colors.onSecondaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quick action icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onOpenLeaderboard,
                    modifier = Modifier.size(32.dp).testTag("leaderboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = "Таблица рекордов",
                        tint = colors.iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier.size(32.dp).testTag("theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkTheme) "Светлая тема" else "Тёмная тема",
                        tint = colors.iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier.size(32.dp).testTag("sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                        contentDescription = "Звук",
                        tint = if (isMuted) colors.textTertiary else colors.iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onTogglePause,
                    modifier = Modifier.size(32.dp).testTag("pause_button")
                ) {
                    Icon(
                        imageVector = if (gameState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (gameState.isPaused) "Продолжить" else "Пауза",
                        tint = colors.iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onRestart,
                    modifier = Modifier.size(32.dp).testTag("restart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Перезапустить",
                        tint = colors.textTertiary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PiecePreviewBox(
    title: String,
    piece: Piece?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = LocalGameColors.current

    Column(
        modifier = modifier
            .shadow(if (colors.isDark) 0.dp else 1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(colors.previewBg)
            .border(
                width = 1.dp,
                color = colors.previewBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            if (piece != null) {
                val shape = piece.shape
                val rows = shape.size
                val cols = shape[0].size
                val miniCellSize = 12.dp

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    for (r in 0 until rows) {
                        Row {
                            for (c in 0 until cols) {
                                if (shape[r][c]) {
                                    AnimalCube(
                                        animal = piece.animal,
                                        size = miniCellSize,
                                        alpha = if (enabled) 1f else 0.4f
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(miniCellSize))
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "—",
                    color = colors.textTertiary,
                    fontSize = 16.sp
                )
            }
        }
    }
}
