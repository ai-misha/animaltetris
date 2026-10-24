package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.ScoreRecord
import com.example.model.Difficulty
import com.example.ui.theme.LocalGameColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LeaderboardDialog(
    currentDifficulty: Difficulty,
    scores: List<ScoreRecord>,
    onSelectDifficulty: (Difficulty) -> Unit,
    onClearDifficultyScores: (Difficulty) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalGameColors.current
    var showConfirmClear by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.dialogBg,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Таблица рекордов",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (scores.isNotEmpty()) {
                    IconButton(
                        onClick = { showConfirmClear = true },
                        modifier = Modifier.size(36.dp).testTag("clear_scores_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Очистить рекорды",
                            tint = colors.textTertiary
                        )
                    }
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Sleek Difficulty Tabs
                ScrollableTabRow(
                    selectedTabIndex = currentDifficulty.ordinal,
                    containerColor = colors.surfaceContainer,
                    contentColor = colors.primary,
                    edgePadding = 4.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[currentDifficulty.ordinal]),
                            color = colors.primary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                ) {
                    Difficulty.entries.forEach { diff ->
                        Tab(
                            selected = diff == currentDifficulty,
                            onClick = { onSelectDifficulty(diff) },
                            text = {
                                Text(
                                    text = "${diff.badgeIcon} ${diff.titleRu}",
                                    fontWeight = if (diff == currentDifficulty) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (diff == currentDifficulty) colors.primary else colors.textSecondary
                                )
                            },
                            modifier = Modifier.testTag("tab_${diff.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Score list or empty state
                if (scores.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🐾", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Рекордов пока нет",
                                color = colors.textPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Сыграйте партию на уровне «${currentDifficulty.titleRu}» и станьте первым!",
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(scores) { index, item ->
                            LeaderboardRowItem(rank = index + 1, item = item)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("leaderboard_close_button")
            ) {
                Text(
                    text = "Закрыть",
                    color = colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    )

    if (showConfirmClear) {
        AlertDialog(
            onDismissRequest = { showConfirmClear = false },
            containerColor = colors.dialogBg,
            title = { Text("Очистить рекорды?", color = colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Удалить все рекорды для уровня «${currentDifficulty.titleRu}»?",
                    color = colors.textSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearDifficultyScores(currentDifficulty)
                        showConfirmClear = false
                    }
                ) {
                    Text("Удалить", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmClear = false }) {
                    Text("Отмена", color = colors.textTertiary)
                }
            }
        )
    }
}

@Composable
private fun LeaderboardRowItem(
    rank: Int,
    item: ScoreRecord
) {
    val colors = LocalGameColors.current

    val badgeBg = when (rank) {
        1 -> colors.primaryContainer
        2 -> colors.secondaryContainer
        3 -> colors.surfaceContainer
        else -> colors.surfaceContainer
    }
    val badgeText = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "$rank"
    }

    val dateFormatter = remember { SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(item.timestamp) { dateFormatter.format(Date(item.timestamp)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surfaceContainer)
            .border(
                width = if (rank <= 3) 1.5.dp else 1.dp,
                color = if (rank <= 3) colors.primary.copy(alpha = 0.5f) else colors.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank badge
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(badgeBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeText,
                fontSize = if (rank <= 3) 16.sp else 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (rank <= 3) colors.onPrimaryContainer else colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Name and details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.playerName,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Линий: ${item.linesCleared} • Ур: ${item.level}",
                    color = colors.textSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formattedDate,
                    color = colors.textTertiary,
                    fontSize = 10.sp
                )
            }
        }

        // Score
        Text(
            text = String.format("%,d", item.score),
            color = colors.primary,
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
