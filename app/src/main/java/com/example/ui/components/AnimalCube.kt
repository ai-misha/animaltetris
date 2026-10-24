package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.AnimalType

@Composable
fun AnimalCube(
    animal: AnimalType,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    isGhost: Boolean = false,
    isJumping: Boolean = false,
    jumpYOffset: Float = 0f,
    jumpRotation: Float = 0f,
    jumpScale: Float = 1f,
    alpha: Float = 1f
) {
    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            if (w <= 0 || h <= 0) return@Canvas

            val effectiveAlpha = if (isGhost) 0.32f else alpha

            rotate(degrees = jumpRotation, pivot = Offset(w / 2f, h / 2f)) {
                scale(scale = jumpScale, pivot = Offset(w / 2f, h / 2f)) {
                    val drawY = jumpYOffset

                    // 1. Draw cube body
                    drawCubeBody(
                        animal = animal,
                        w = w,
                        h = h,
                        yOffset = drawY,
                        alpha = effectiveAlpha,
                        isGhost = isGhost
                    )

                    // 2. Draw animal face details
                    if (!isGhost) {
                        drawAnimalFace(
                            animal = animal,
                            w = w,
                            h = h,
                            yOffset = drawY,
                            isJumping = isJumping,
                            alpha = effectiveAlpha
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawCubeBody(
    animal: AnimalType,
    w: Float,
    h: Float,
    yOffset: Float,
    alpha: Float,
    isGhost: Boolean
) {
    val corner = w * 0.22f
    val pad = w * 0.05f
    val rectW = w - pad * 2
    val rectH = h - pad * 2
    val topX = pad
    val topY = pad + yOffset

    if (isGhost) {
        // Ghost outline
        drawRoundRect(
            color = animal.primaryColor.copy(alpha = alpha),
            topLeft = Offset(topX, topY),
            size = Size(rectW, rectH),
            cornerRadius = CornerRadius(corner, corner),
            style = Stroke(width = w * 0.1f)
        )
        return
    }

    // Gradient background
    val brush = Brush.linearGradient(
        colors = listOf(
            animal.highlightColor.copy(alpha = alpha),
            animal.primaryColor.copy(alpha = alpha),
            animal.darkShadowColor.copy(alpha = alpha)
        ),
        start = Offset(topX, topY),
        end = Offset(topX + rectW, topY + rectH)
    )

    drawRoundRect(
        brush = brush,
        topLeft = Offset(topX, topY),
        size = Size(rectW, rectH),
        cornerRadius = CornerRadius(corner, corner)
    )

    // Highlight border on top-left for juicy 3D bevel look
    val highlightPath = Path().apply {
        moveTo(topX + corner, topY + 1.5f)
        lineTo(topX + rectW - corner, topY + 1.5f)
    }
    drawPath(
        path = highlightPath,
        color = Color.White.copy(alpha = 0.55f * alpha),
        style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawAnimalFace(
    animal: AnimalType,
    w: Float,
    h: Float,
    yOffset: Float,
    isJumping: Boolean,
    alpha: Float
) {
    val cx = w / 2f
    val cy = (h / 2f) + yOffset
    val eyeColor = Color(0xFF212121).copy(alpha = alpha)
    val cheekColor = Color(0xFFFF8A80).copy(alpha = 0.75f * alpha)
    val white = Color.White.copy(alpha = alpha)

    when (animal) {
        AnimalType.FROG -> {
            // Big frog eyes peeking on top
            val eyeR = w * 0.16f
            val eyeSpacing = w * 0.23f
            val eyeY = cy - h * 0.16f
            // Left eye white
            drawCircle(color = white, radius = eyeR, center = Offset(cx - eyeSpacing, eyeY))
            // Right eye white
            drawCircle(color = white, radius = eyeR, center = Offset(cx + eyeSpacing, eyeY))

            if (isJumping) {
                // Happy squinting eyes ^ ^
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), eyeR * 0.9f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), eyeR * 0.9f, eyeColor)
            } else {
                // Pupils
                drawCircle(color = eyeColor, radius = eyeR * 0.55f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = eyeR * 0.55f, center = Offset(cx + eyeSpacing, eyeY))
                // Eye highlights
                drawCircle(color = white, radius = eyeR * 0.2f, center = Offset(cx - eyeSpacing - eyeR * 0.2f, eyeY - eyeR * 0.2f))
                drawCircle(color = white, radius = eyeR * 0.2f, center = Offset(cx + eyeSpacing - eyeR * 0.2f, eyeY - eyeR * 0.2f))
            }

            // Blushing cheeks
            drawCircle(color = cheekColor, radius = w * 0.09f, center = Offset(cx - w * 0.28f, cy + h * 0.12f))
            drawCircle(color = cheekColor, radius = w * 0.09f, center = Offset(cx + w * 0.28f, cy + h * 0.12f))

            // Smile
            val smilePath = Path().apply {
                moveTo(cx - w * 0.14f, cy + h * 0.08f)
                quadraticTo(cx, cy + (if (isJumping) h * 0.25f else h * 0.18f), cx + w * 0.14f, cy + h * 0.08f)
            }
            drawPath(path = smilePath, color = eyeColor, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))
        }

        AnimalType.CAT -> {
            // Triangle ears
            val earSize = w * 0.18f
            val earY = cy - h * 0.35f
            // Left ear
            val leftEar = Path().apply {
                moveTo(cx - w * 0.32f, cy - h * 0.15f)
                lineTo(cx - w * 0.25f, earY)
                lineTo(cx - w * 0.1f, cy - h * 0.25f)
                close()
            }
            drawPath(path = leftEar, color = animal.darkShadowColor.copy(alpha = alpha))
            // Right ear
            val rightEar = Path().apply {
                moveTo(cx + w * 0.1f, cy - h * 0.25f)
                lineTo(cx + w * 0.25f, earY)
                lineTo(cx + w * 0.32f, cy - h * 0.15f)
                close()
            }
            drawPath(path = rightEar, color = animal.darkShadowColor.copy(alpha = alpha))

            // Eyes
            val eyeY = cy - h * 0.05f
            val eyeSpacing = w * 0.2f
            if (isJumping) {
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), w * 0.12f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), w * 0.12f, eyeColor)
            } else {
                drawCircle(color = eyeColor, radius = w * 0.09f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = w * 0.09f, center = Offset(cx + eyeSpacing, eyeY))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx - eyeSpacing - 1.5f, eyeY - 1.5f))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx + eyeSpacing - 1.5f, eyeY - 1.5f))
            }

            // Nose
            drawCircle(color = Color(0xFFD84315).copy(alpha = alpha), radius = w * 0.045f, center = Offset(cx, cy + h * 0.06f))

            // Cheeks
            drawCircle(color = cheekColor, radius = w * 0.08f, center = Offset(cx - w * 0.26f, cy + h * 0.14f))
            drawCircle(color = cheekColor, radius = w * 0.08f, center = Offset(cx + w * 0.26f, cy + h * 0.14f))

            // Cat mouth :3
            val mouthPath = Path().apply {
                moveTo(cx - w * 0.1f, cy + h * 0.18f)
                quadraticTo(cx - w * 0.05f, cy + h * 0.22f, cx, cy + h * 0.16f)
                quadraticTo(cx + w * 0.05f, cy + h * 0.22f, cx + w * 0.1f, cy + h * 0.18f)
            }
            drawPath(path = mouthPath, color = eyeColor, style = Stroke(width = w * 0.055f, cap = StrokeCap.Round))
        }

        AnimalType.PIGGY -> {
            // Snout oval
            val snoutW = w * 0.42f
            val snoutH = h * 0.28f
            drawRoundRect(
                color = Color(0xFFF48FB1).copy(alpha = alpha),
                topLeft = Offset(cx - snoutW / 2f, cy - snoutH / 2f + h * 0.08f),
                size = Size(snoutW, snoutH),
                cornerRadius = CornerRadius(snoutH / 2f, snoutH / 2f)
            )
            // Nostrils
            val nostrilY = cy + h * 0.08f
            drawCircle(color = Color(0xFF880E4F).copy(alpha = alpha), radius = w * 0.04f, center = Offset(cx - w * 0.08f, nostrilY))
            drawCircle(color = Color(0xFF880E4F).copy(alpha = alpha), radius = w * 0.04f, center = Offset(cx + w * 0.08f, nostrilY))

            // Eyes
            val eyeY = cy - h * 0.15f
            val eyeSpacing = w * 0.22f
            if (isJumping) {
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), w * 0.12f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), w * 0.12f, eyeColor)
            } else {
                drawCircle(color = eyeColor, radius = w * 0.085f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = w * 0.085f, center = Offset(cx + eyeSpacing, eyeY))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx - eyeSpacing - 1.5f, eyeY - 1.5f))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx + eyeSpacing - 1.5f, eyeY - 1.5f))
            }

            // Cheeks
            drawCircle(color = Color(0xFFFF4081).copy(alpha = 0.8f * alpha), radius = w * 0.09f, center = Offset(cx - w * 0.28f, cy + h * 0.15f))
            drawCircle(color = Color(0xFFFF4081).copy(alpha = 0.8f * alpha), radius = w * 0.09f, center = Offset(cx + w * 0.28f, cy + h * 0.15f))
        }

        AnimalType.PANDA -> {
            // Black panda ears
            val earR = w * 0.13f
            drawCircle(color = eyeColor, radius = earR, center = Offset(cx - w * 0.28f, cy - h * 0.28f))
            drawCircle(color = eyeColor, radius = earR, center = Offset(cx + w * 0.28f, cy - h * 0.28f))

            // Eye patches
            val patchW = w * 0.24f
            val patchH = h * 0.28f
            val patchY = cy - h * 0.08f
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(cx - w * 0.32f, patchY - patchH / 2f),
                size = Size(patchW, patchH),
                cornerRadius = CornerRadius(patchW / 2f, patchH / 2f)
            )
            drawRoundRect(
                color = eyeColor,
                topLeft = Offset(cx + w * 0.08f, patchY - patchH / 2f),
                size = Size(patchW, patchH),
                cornerRadius = CornerRadius(patchW / 2f, patchH / 2f)
            )

            // Eyes inside patches
            if (isJumping) {
                drawHappySquint(Offset(cx - w * 0.2f, patchY), w * 0.08f, white)
                drawHappySquint(Offset(cx + w * 0.2f, patchY), w * 0.08f, white)
            } else {
                drawCircle(color = white, radius = w * 0.06f, center = Offset(cx - w * 0.2f, patchY))
                drawCircle(color = white, radius = w * 0.06f, center = Offset(cx + w * 0.2f, patchY))
            }

            // Snout & nose
            drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(cx, cy + h * 0.1f))
            // Mouth
            val pandaMouth = Path().apply {
                moveTo(cx - w * 0.08f, cy + h * 0.18f)
                quadraticTo(cx, cy + (if (isJumping) h * 0.26f else h * 0.22f), cx + w * 0.08f, cy + h * 0.18f)
            }
            drawPath(path = pandaMouth, color = eyeColor, style = Stroke(width = w * 0.05f, cap = StrokeCap.Round))
        }

        AnimalType.BEAR -> {
            // Round ears
            val earR = w * 0.14f
            drawCircle(color = animal.darkShadowColor.copy(alpha = alpha), radius = earR, center = Offset(cx - w * 0.28f, cy - h * 0.28f))
            drawCircle(color = animal.darkShadowColor.copy(alpha = alpha), radius = earR, center = Offset(cx + w * 0.28f, cy - h * 0.28f))

            // Muzzle oval
            val muzzleW = w * 0.44f
            val muzzleH = h * 0.32f
            drawRoundRect(
                color = Color(0xFFD7CCC8).copy(alpha = alpha),
                topLeft = Offset(cx - muzzleW / 2f, cy - muzzleH / 2f + h * 0.12f),
                size = Size(muzzleW, muzzleH),
                cornerRadius = CornerRadius(muzzleH / 2f, muzzleH / 2f)
            )

            // Nose
            drawCircle(color = Color(0xFF3E2723).copy(alpha = alpha), radius = w * 0.05f, center = Offset(cx, cy + h * 0.06f))

            // Eyes
            val eyeY = cy - h * 0.12f
            val eyeSpacing = w * 0.21f
            if (isJumping) {
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), w * 0.11f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), w * 0.11f, eyeColor)
            } else {
                drawCircle(color = eyeColor, radius = w * 0.075f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = w * 0.075f, center = Offset(cx + eyeSpacing, eyeY))
                drawCircle(color = white, radius = w * 0.03f, center = Offset(cx - eyeSpacing - 1.5f, eyeY - 1.5f))
                drawCircle(color = white, radius = w * 0.03f, center = Offset(cx + eyeSpacing - 1.5f, eyeY - 1.5f))
            }

            // Smile
            val bearMouth = Path().apply {
                moveTo(cx - w * 0.08f, cy + h * 0.18f)
                quadraticTo(cx, cy + (if (isJumping) h * 0.25f else h * 0.21f), cx + w * 0.08f, cy + h * 0.18f)
            }
            drawPath(path = bearMouth, color = Color(0xFF3E2723).copy(alpha = alpha), style = Stroke(width = w * 0.05f, cap = StrokeCap.Round))
        }

        AnimalType.BUNNY -> {
            // Long Bunny ears
            val earW = w * 0.12f
            val earH = h * 0.36f
            val earY = cy - h * 0.42f
            // Left ear
            drawRoundRect(
                color = animal.highlightColor.copy(alpha = alpha),
                topLeft = Offset(cx - w * 0.22f, earY),
                size = Size(earW, earH),
                cornerRadius = CornerRadius(earW / 2f, earW / 2f)
            )
            // Right ear
            drawRoundRect(
                color = animal.highlightColor.copy(alpha = alpha),
                topLeft = Offset(cx + w * 0.10f, earY),
                size = Size(earW, earH),
                cornerRadius = CornerRadius(earW / 2f, earW / 2f)
            )

            // Eyes
            val eyeY = cy - h * 0.06f
            val eyeSpacing = w * 0.2f
            if (isJumping) {
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), w * 0.11f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), w * 0.11f, eyeColor)
            } else {
                drawCircle(color = eyeColor, radius = w * 0.08f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = w * 0.08f, center = Offset(cx + eyeSpacing, eyeY))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx - eyeSpacing - 1.5f, eyeY - 1.5f))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx + eyeSpacing - 1.5f, eyeY - 1.5f))
            }

            // Nose
            drawCircle(color = Color(0xFFFF80AB).copy(alpha = alpha), radius = w * 0.045f, center = Offset(cx, cy + h * 0.08f))

            // Bunny smile / cheeks
            drawCircle(color = Color(0xFFFF80AB).copy(alpha = 0.7f * alpha), radius = w * 0.08f, center = Offset(cx - w * 0.26f, cy + h * 0.14f))
            drawCircle(color = Color(0xFFFF80AB).copy(alpha = 0.7f * alpha), radius = w * 0.08f, center = Offset(cx + w * 0.26f, cy + h * 0.14f))
        }

        AnimalType.FOX -> {
            // Fox ears
            val earSize = w * 0.2f
            val earY = cy - h * 0.38f
            val leftEar = Path().apply {
                moveTo(cx - w * 0.32f, cy - h * 0.14f)
                lineTo(cx - w * 0.22f, earY)
                lineTo(cx - w * 0.06f, cy - h * 0.22f)
                close()
            }
            drawPath(path = leftEar, color = animal.darkShadowColor.copy(alpha = alpha))

            val rightEar = Path().apply {
                moveTo(cx + w * 0.06f, cy - h * 0.22f)
                lineTo(cx + w * 0.22f, earY)
                lineTo(cx + w * 0.32f, cy - h * 0.14f)
                close()
            }
            drawPath(path = rightEar, color = animal.darkShadowColor.copy(alpha = alpha))

            // White cheek fluff
            val fluffL = Path().apply {
                moveTo(cx - w * 0.4f, cy + h * 0.2f)
                lineTo(cx - w * 0.1f, cy + h * 0.35f)
                lineTo(cx, cy + h * 0.14f)
                close()
            }
            drawPath(path = fluffL, color = Color.White.copy(alpha = 0.85f * alpha))

            val fluffR = Path().apply {
                moveTo(cx + w * 0.4f, cy + h * 0.2f)
                lineTo(cx + w * 0.1f, cy + h * 0.35f)
                lineTo(cx, cy + h * 0.14f)
                close()
            }
            drawPath(path = fluffR, color = Color.White.copy(alpha = 0.85f * alpha))

            // Eyes
            val eyeY = cy - h * 0.05f
            val eyeSpacing = w * 0.19f
            if (isJumping) {
                drawHappySquint(Offset(cx - eyeSpacing, eyeY), w * 0.11f, eyeColor)
                drawHappySquint(Offset(cx + eyeSpacing, eyeY), w * 0.11f, eyeColor)
            } else {
                drawCircle(color = eyeColor, radius = w * 0.08f, center = Offset(cx - eyeSpacing, eyeY))
                drawCircle(color = eyeColor, radius = w * 0.08f, center = Offset(cx + eyeSpacing, eyeY))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx - eyeSpacing - 1.5f, eyeY - 1.5f))
                drawCircle(color = white, radius = w * 0.035f, center = Offset(cx + eyeSpacing - 1.5f, eyeY - 1.5f))
            }

            // Nose
            drawCircle(color = eyeColor, radius = w * 0.05f, center = Offset(cx, cy + h * 0.14f))
        }
    }
}

private fun DrawScope.drawHappySquint(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x - size, center.y + size * 0.3f)
        quadraticTo(center.x, center.y - size * 0.8f, center.x + size, center.y + size * 0.3f)
    }
    drawPath(path = path, color = color, style = Stroke(width = size * 0.4f, cap = StrokeCap.Round))
}
