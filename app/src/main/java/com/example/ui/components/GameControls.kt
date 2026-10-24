package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GameColorScheme
import com.example.ui.theme.LocalGameColors

@Composable
fun GameControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onHold: () -> Unit,
    canHold: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = LocalGameColors.current

    // Expressive footer: a large-radius tonal sheet anchored to the bottom edge
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.surface,
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
        shadowElevation = if (colors.isDark) 2.dp else 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left group: Hold (Запас) & Soft Drop (Вниз)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlCircleButton(
                    icon = Icons.Default.SwapHoriz,
                    label = "Запас",
                    enabled = canHold,
                    onClick = onHold,
                    testTag = "hold_button"
                )

                ControlCircleButton(
                    icon = Icons.Default.ArrowDownward,
                    label = "Вниз",
                    enabled = true,
                    onClick = onSoftDrop,
                    testTag = "soft_drop_button"
                )
            }

            // Center group: Left/Right arrows + Drop button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.width(140.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalIconButton(
                        onClick = onMoveLeft,
                        modifier = Modifier.size(52.dp).testTag("move_left_button"),
                        shape = MaterialTheme.shapes.medium,
                        colors = circleButtonColors(colors.circleBtnBg, colors.circleBtnContent, colors)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Влево",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    FilledTonalIconButton(
                        onClick = onMoveRight,
                        modifier = Modifier.size(52.dp).testTag("move_right_button"),
                        shape = MaterialTheme.shapes.medium,
                        colors = circleButtonColors(colors.circleBtnBg, colors.circleBtnContent, colors)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Вправо",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Drop button (СБРОС): full-width pill with a bold label
                Button(
                    onClick = onHardDrop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("hard_drop_button"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.dropBtnBg,
                        contentColor = colors.dropBtnContent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "СБРОС",
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // Right group: Rotate (ПОВОРОТ) as the expressive "hero" button
            FilledIconButton(
                onClick = onRotate,
                modifier = Modifier.size(72.dp).testTag("rotate_button"),
                shape = MaterialTheme.shapes.large,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = colors.rotateBtnBg,
                    contentColor = colors.rotateBtnContent
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = "Поворот",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "ПОВОРОТ",
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlCircleButton(
    icon: ImageVector,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val colors = LocalGameColors.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalIconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.size(52.dp).testTag(testTag),
            shape = CircleShape,
            colors = circleButtonColors(colors.circleBtnBg, colors.circleBtnContent, colors)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) colors.textSecondary else colors.textTertiary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun circleButtonColors(
    container: Color,
    content: Color,
    colors: GameColorScheme
) =IconButtonDefaults.filledTonalIconButtonColors(
    containerColor = container,
    contentColor = content,
    disabledContainerColor = colors.secondaryContainer.copy(alpha = 0.5f),
    disabledContentColor = colors.textTertiary
)
