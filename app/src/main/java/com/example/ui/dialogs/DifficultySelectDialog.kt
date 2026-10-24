package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.ui.theme.LocalGameColors

@Composable
fun DifficultySelectDialog(
    currentDifficulty: Difficulty,
    onSelectDifficulty: (Difficulty) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalGameColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.dialogBg,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text(
                text = "Уровень сложности",
                color = colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Difficulty.entries.forEach { diff ->
                    val isSelected = diff == currentDifficulty
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) colors.primaryContainer else colors.surfaceContainer)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) colors.primary else colors.outline.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectDifficulty(diff) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelectDifficulty(diff) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colors.primary,
                                unselectedColor = colors.textTertiary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${diff.badgeIcon} ${diff.titleRu}",
                                    color = colors.textPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "x${diff.scoreMultiplier}",
                                    color = colors.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = diff.descriptionRu,
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("difficulty_dialog_close")
            ) {
                Text("Закрыть", color = colors.primary, fontWeight = FontWeight.Bold)
            }
        }
    )
}
