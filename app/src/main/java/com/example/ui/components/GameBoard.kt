package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AnimalType
import com.example.model.ClearingState
import com.example.model.GameState
import com.example.model.Piece
import com.example.ui.theme.LocalGameColors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun GameBoard(
    gameState: GameState,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalGameColors.current
    var dragAccumulatorX by remember { mutableFloatStateOf(0f) }
    var dragAccumulatorY by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(10f / 20f)
            .clip(RoundedCornerShape(26.dp))
            .background(colors.surfaceContainer)
            .border(
                width = 3.5.dp,
                color = colors.surfaceVariant,
                shape = RoundedCornerShape(26.dp)
            )
            .pointerInput(gameState.isGameOver, gameState.isPaused) {
                if (gameState.isGameOver || gameState.isPaused) return@pointerInput

                detectTapGestures(
                    onTap = { onRotate() },
                    onDoubleTap = { onHardDrop() }
                )
            }
            .pointerInput(gameState.isGameOver, gameState.isPaused) {
                if (gameState.isGameOver || gameState.isPaused) return@pointerInput

                detectDragGestures(
                    onDragStart = {
                        dragAccumulatorX = 0f
                        dragAccumulatorY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragAccumulatorX += dragAmount.x
                        dragAccumulatorY += dragAmount.y

                        val thresholdX = 38f
                        val thresholdY = 50f

                        while (dragAccumulatorX > thresholdX) {
                            onMoveRight()
                            dragAccumulatorX -= thresholdX
                        }
                        while (dragAccumulatorX < -thresholdX) {
                            onMoveLeft()
                            dragAccumulatorX += thresholdX
                        }
                        while (dragAccumulatorY > thresholdY) {
                            onSoftDrop()
                            dragAccumulatorY -= thresholdY
                        }
                    }
                )
            }
            .testTag("game_board")
    ) {
        val boardWidthPx = maxWidth
        val cellSize = boardWidthPx / GameState.BOARD_WIDTH.toFloat()

        // Background subtle grid drawing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val w = size.width
                    val h = size.height
                    val colW = w / GameState.BOARD_WIDTH
                    val rowH = h / GameState.BOARD_HEIGHT
                    val gridColor = colors.gridColor

                    // Vertical lines
                    for (c in 1 until GameState.BOARD_WIDTH) {
                        drawLine(
                            color = gridColor,
                            start = Offset(c * colW, 0f),
                            end = Offset(c * colW, h),
                            strokeWidth = 1f
                        )
                    }
                    // Horizontal lines
                    for (r in 1 until GameState.BOARD_HEIGHT) {
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, r * rowH),
                            end = Offset(w, r * rowH),
                            strokeWidth = 1f
                        )
                    }
                }
        )

        // 1. Draw placed cubes on board
        val clearing = gameState.clearingState
        for (r in 0 until GameState.BOARD_HEIGHT) {
            val isClearingRow = clearing?.rowIndices?.contains(r) == true
            for (c in 0 until GameState.BOARD_WIDTH) {
                val animal = gameState.board[r][c]
                if (animal != null) {
                    val (jumpY, jumpRot, jumpScale, jumpAlpha) = if (isClearingRow && clearing != null) {
                        calculateJumpParams(clearing, c)
                    } else {
                        Quad(0f, 0f, 1f, 1f)
                    }

                    AnimalCube(
                        animal = animal,
                        size = cellSize,
                        isJumping = isClearingRow,
                        jumpYOffset = jumpY,
                        jumpRotation = jumpRot,
                        jumpScale = jumpScale,
                        alpha = jumpAlpha,
                        modifier = Modifier
                            .offset(x = cellSize * c, y = cellSize * r)
                    )
                }
            }
        }

        // 2. Draw Ghost Piece (Guide shadow)
        val ghost = gameState.ghostPiece
        if (ghost != null && !gameState.isGameOver && gameState.clearingState == null) {
            DrawPieceOnBoard(piece = ghost, cellSize = cellSize, isGhost = true)
        }

        // 3. Draw Active Falling Piece
        val active = gameState.currentPiece
        if (active != null && !gameState.isGameOver && gameState.clearingState == null) {
            DrawPieceOnBoard(piece = active, cellSize = cellSize, isGhost = false)
        }
    }
}

@Composable
private fun DrawPieceOnBoard(
    piece: Piece,
    cellSize: androidx.compose.ui.unit.Dp,
    isGhost: Boolean
) {
    for (r in piece.shape.indices) {
        for (c in piece.shape[r].indices) {
            if (piece.shape[r][c]) {
                val boardX = piece.x + c
                val boardY = piece.y + r
                if (boardY in 0 until GameState.BOARD_HEIGHT && boardX in 0 until GameState.BOARD_WIDTH) {
                    AnimalCube(
                        animal = piece.animal,
                        size = cellSize,
                        isGhost = isGhost,
                        modifier = Modifier
                            .offset(x = cellSize * boardX, y = cellSize * boardY)
                    )
                }
            }
        }
    }
}

private data class Quad(val y: Float, val rot: Float, val scale: Float, val alpha: Float)

private fun calculateJumpParams(clearing: ClearingState, col: Int): Quad {
    val progress = clearing.animProgress // 0f to 1f
    // Parabolic arc for energetic jump: jumps up by ~60 pixels then descends
    val jumpHeight = 70f
    val jumpY = -sin(progress * PI.toFloat()) * jumpHeight

    // Playful rotation tilt alternating by column
    val dir = if (col % 2 == 0) 1f else -1f
    val rot = dir * (12f + (col % 3) * 5f) * sin(progress * PI.toFloat())

    // Squash & Stretch: squash on launch, stretch in air, pop at end
    val scale = if (progress < 0.7f) {
        1f + 0.22f * sin(progress * PI.toFloat())
    } else {
        // Pop out fade
        (1f - (progress - 0.7f) / 0.3f).coerceAtLeast(0f) * 1.2f
    }

    val alpha = if (progress > 0.65f) {
        (1f - (progress - 0.65f) / 0.35f).coerceIn(0f, 1f)
    } else 1f

    return Quad(jumpY, rot, scale, alpha)
}
