package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.LocalGameColors

@Composable
fun GameOverDialog(
    gameState: GameState,
    hasSavedScore: Boolean,
    onSaveScore: (String) -> Unit,
    onOpenLeaderboard: () -> Unit,
    onRestart: () -> Unit
) {
    val colors = LocalGameColors.current
    var playerName by remember { mutableStateOf("Игрок") }

    AlertDialog(
        onDismissRequest = { /* Modal */ },
        containerColor = colors.dialogBg,
        shape = RoundedCornerShape(28.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🐱 Игра окончена! 🐻",
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Кубики заполнили игровое поле",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sleek Score Summary Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.surfaceContainer)
                        .border(1.dp, colors.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ФИНАЛЬНЫЙ СЧЁТ",
                        color = colors.textTertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = String.format("%,d", gameState.score),
                        color = colors.primary,
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Сложность", color = colors.textTertiary, fontSize = 11.sp)
                            Text(
                                text = "${gameState.difficulty.badgeIcon} ${gameState.difficulty.titleRu}",
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Линии", color = colors.textTertiary, fontSize = 11.sp)
                            Text(
                                text = "${gameState.linesCleared}",
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Уровень", color = colors.textTertiary, fontSize = 11.sp)
                            Text(
                                text = "${gameState.level}",
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!hasSavedScore) {
                    // Save score section
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { if (it.length <= 16) playerName = it },
                        label = { Text("Имя игрока") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            focusedLabelColor = colors.primary,
                            unfocusedLabelColor = colors.textTertiary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_name_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val trimmed = playerName.trim().ifEmpty { "Игрок" }
                            onSaveScore(trimmed)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_score_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Сохранить в рекорды", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.primaryContainer)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ Рекорд успешно сохранён!",
                            color = colors.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("game_over_restart_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Играть снова", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onOpenLeaderboard,
                modifier = Modifier.testTag("game_over_leaderboard_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Leaderboard,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Таблица рекордов", color = colors.primary, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
