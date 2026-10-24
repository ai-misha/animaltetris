package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Top Bar: App avatar + title & level on left, score tile on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Expressive avatar: a bold tonal blob with the animal icon
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = "Animal Tetris",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = "Animal Tetris",
                        color = colors.textPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Уровень ${String.format("%02d", gameState.level)} • Линий: ${gameState.linesCleared}",
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Score tile: large rounded container with a heavy numeric readout
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if (colors.isDark) colors.outline.copy(alpha = 0.5f) else Color.Transparent,
                    shape = MaterialTheme.shapes.large
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "СЧЁТ",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall,
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // 2. Second Row: Difficulty chips + quick action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Difficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = diff == gameState.difficulty,
                        onClick = { onSelectDifficulty(diff) },
                        label = {
                            Text(
                                text = diff.titleRu,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        shape = CircleShape,
                        modifier = Modifier.testTag("chip_${diff.name.lowercase()}")
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = onOpenLeaderboard,
                    modifier = Modifier.size(40.dp).testTag("leaderboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = "Таблица рекордов",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier.size(40.dp).testTag("theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkTheme) "Светлая тема" else "Тёмная тема",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = onToggleMute,
                    modifier = Modifier.size(40.dp).testTag("sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                        contentDescription = "Звук",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = onTogglePause,
                    modifier = Modifier.size(40.dp).testTag("pause_button")
                ) {
                    Icon(
                        imageVector = if (gameState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (gameState.isPaused) "Продолжить" else "Пауза",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = onRestart,
                    modifier = Modifier.size(40.dp).testTag("restart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Перезапустить",
                        modifier = Modifier.size(20.dp)
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
            .background(colors.previewBg, MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = colors.previewBorder,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
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
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
