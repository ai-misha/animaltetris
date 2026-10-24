package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Sleek Interface Footer: elevated surface with rounded top (32.dp) and shadow
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (colors.isDark) 4.dp else 12.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group: Hold (Запас) & Soft Drop (Вниз)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hold Button
                ControlCircleButton(
                    icon = Icons.Default.SwapHoriz,
                    label = "Запас",
                    backgroundColor = if (canHold) colors.circleBtnBg else colors.secondaryContainer.copy(alpha = 0.5f),
                    contentColor = if (canHold) colors.circleBtnContent else colors.textTertiary,
                    enabled = canHold,
                    onClick = onHold,
                    testTag = "hold_button"
                )

                // Soft Drop (Вниз) Button
                ControlCircleButton(
                    icon = Icons.Default.ArrowDownward,
                    label = "Вниз",
                    backgroundColor = colors.circleBtnBg,
                    contentColor = colors.circleBtnContent,
                    enabled = true,
                    onClick = onSoftDrop,
                    testTag = "soft_drop_button"
                )
            }

            // Center Group: Left/Right arrows + Drop button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.width(136.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Move Left
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(colors.circleBtnBg)
                            .clickable(onClick = onMoveLeft)
                            .testTag("move_left_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Влево",
                            tint = colors.circleBtnContent,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Move Right
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(colors.circleBtnBg)
                            .clickable(onClick = onMoveRight)
                            .testTag("move_right_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Вправо",
                            tint = colors.circleBtnContent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Drop Button (СБРОС)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.dropBtnBg)
                        .clickable(onClick = onHardDrop)
                        .testTag("hard_drop_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardDoubleArrowDown,
                            contentDescription = null,
                            tint = colors.dropBtnContent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "СБРОС",
                            color = colors.dropBtnContent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                }
            }

            // Right Group: Rotate button (ПОВОРОТ)
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(colors.rotateBtnBg)
                    .clickable(onClick = onRotate)
                    .testTag("rotate_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = "Поворот",
                        tint = colors.rotateBtnContent,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "ПОВОРОТ",
                        color = colors.rotateBtnContent,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
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
    backgroundColor: Color,
    contentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val colors = LocalGameColors.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(enabled = enabled, onClick = onClick)
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (enabled) colors.textSecondary else colors.textTertiary,
            fontWeight = FontWeight.Medium
        )
    }
}
