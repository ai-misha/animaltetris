package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GameBoard
import com.example.ui.components.GameControls
import com.example.ui.components.PiecePreviewBox
import com.example.ui.components.ScoreHeader
import com.example.ui.dialogs.DifficultySelectDialog
import com.example.ui.dialogs.GameOverDialog
import com.example.ui.dialogs.LeaderboardDialog
import com.example.ui.theme.LocalGameColors

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val showLeaderboard by viewModel.showLeaderboard.collectAsStateWithLifecycle()
    val showDifficultySelect by viewModel.showDifficultySelect.collectAsStateWithLifecycle()
    val leaderboardDifficulty by viewModel.leaderboardDifficulty.collectAsStateWithLifecycle()
    val topScores by viewModel.topScores.collectAsStateWithLifecycle()
    val hasSavedScore by viewModel.hasSavedScore.collectAsStateWithLifecycle()

    val colors = LocalGameColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bg)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Sleek Header (App Info, Level, Score, Chips, Top Controls & Theme Toggle)
            ScoreHeader(
                gameState = gameState,
                isMuted = isMuted,
                isDarkTheme = isDarkTheme,
                onToggleMute = { viewModel.toggleMute() },
                onToggleTheme = { viewModel.toggleTheme() },
                onTogglePause = { viewModel.togglePause() },
                onOpenLeaderboard = { viewModel.openLeaderboard() },
                onSelectDifficulty = { diff -> viewModel.selectDifficultyAndRestart(diff) },
                onRestart = { viewModel.startGame() },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // 2. Play Area (Hold preview on left, Board in center, Next preview on right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side: Hold piece
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    PiecePreviewBox(
                        title = "ЗАПАС",
                        piece = gameState.holdPiece,
                        enabled = gameState.canHold,
                        modifier = Modifier.width(66.dp)
                    )
                    if (gameState.holdPiece != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = gameState.holdPiece!!.animal.emoji,
                            fontSize = 18.sp
                        )
                    }
                }

                // Center: Main Game Board
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    GameBoard(
                        gameState = gameState,
                        onMoveLeft = { viewModel.moveLeft() },
                        onMoveRight = { viewModel.moveRight() },
                        onRotate = { viewModel.rotatePiece() },
                        onSoftDrop = { viewModel.softDrop() },
                        onHardDrop = { viewModel.hardDrop() }
                    )

                    // Pause overlay
                    if (gameState.isPaused && !gameState.isGameOver) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF0F0D13).copy(alpha = 0.82f))
                                .clickable { viewModel.togglePause() }
                                .testTag("pause_overlay"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Продолжить",
                                    tint = colors.primary,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ПАУЗА",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Нажмите, чтобы продолжить",
                                    color = colors.primaryContainer,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Right Side: Next piece
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    PiecePreviewBox(
                        title = "ДАЛЬШЕ",
                        piece = gameState.nextPiece,
                        enabled = true,
                        modifier = Modifier.width(66.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = gameState.nextPiece.animal.emoji,
                        fontSize = 18.sp
                    )
                }
            }

            // 3. Sleek Interface Bottom Controls
            GameControls(
                onMoveLeft = { viewModel.moveLeft() },
                onMoveRight = { viewModel.moveRight() },
                onRotate = { viewModel.rotatePiece() },
                onSoftDrop = { viewModel.softDrop() },
                onHardDrop = { viewModel.hardDrop() },
                onHold = { viewModel.holdPiece() },
                canHold = gameState.canHold
            )
        }

        // Dialogs
        if (gameState.isGameOver) {
            GameOverDialog(
                gameState = gameState,
                hasSavedScore = hasSavedScore,
                onSaveScore = { name -> viewModel.saveScore(name) },
                onOpenLeaderboard = { viewModel.openLeaderboard() },
                onRestart = { viewModel.startGame() }
            )
        }

        if (showLeaderboard) {
            LeaderboardDialog(
                currentDifficulty = leaderboardDifficulty,
                scores = topScores,
                onSelectDifficulty = { diff -> viewModel.setLeaderboardDifficulty(diff) },
                onClearDifficultyScores = { diff -> viewModel.clearDifficultyScores(diff) },
                onDismiss = { viewModel.closeLeaderboard() }
            )
        }

        if (showDifficultySelect) {
            DifficultySelectDialog(
                currentDifficulty = gameState.difficulty,
                onSelectDifficulty = { diff -> viewModel.selectDifficultyAndRestart(diff) },
                onDismiss = { viewModel.closeDifficultySelect() }
            )
        }
    }
}
