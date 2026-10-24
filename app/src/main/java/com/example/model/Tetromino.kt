package com.example.model

enum class TetrominoType(val animal: AnimalType) {
    I(AnimalType.FROG),
    O(AnimalType.CAT),
    T(AnimalType.PIGGY),
    S(AnimalType.PANDA),
    Z(AnimalType.BEAR),
    J(AnimalType.BUNNY),
    L(AnimalType.FOX)
}

data class Piece(
    val type: TetrominoType,
    val x: Int,
    val y: Int,
    val rotation: Int = 0,
    val shape: List<List<Boolean>> = getShapeFor(type, rotation)
) {
    val animal: AnimalType get() = type.animal

    fun rotatedClockwise(): Piece {
        val nextRotation = (rotation + 1) % 4
        return copy(
            rotation = nextRotation,
            shape = getShapeFor(type, nextRotation)
        )
    }

    fun rotatedCounterClockwise(): Piece {
        val nextRotation = if (rotation == 0) 3 else rotation - 1
        return copy(
            rotation = nextRotation,
            shape = getShapeFor(type, nextRotation)
        )
    }

    companion object {
        fun createRandom(): Piece {
            val type = TetrominoType.entries.random()
            val startX = when (type) {
                TetrominoType.O -> 4
                TetrominoType.I -> 3
                else -> 3
            }
            return Piece(type = type, x = startX, y = 0, rotation = 0)
        }

        fun create(type: TetrominoType): Piece {
            val startX = when (type) {
                TetrominoType.O -> 4
                TetrominoType.I -> 3
                else -> 3
            }
            return Piece(type = type, x = startX, y = 0, rotation = 0)
        }

        fun getShapeFor(type: TetrominoType, rotation: Int): List<List<Boolean>> {
            val base: List<List<Boolean>> = when (type) {
                TetrominoType.I -> listOf(
                    listOf(false, false, false, false),
                    listOf(true, true, true, true),
                    listOf(false, false, false, false),
                    listOf(false, false, false, false)
                )
                TetrominoType.O -> listOf(
                    listOf(true, true),
                    listOf(true, true)
                )
                TetrominoType.T -> listOf(
                    listOf(false, true, false),
                    listOf(true, true, true),
                    listOf(false, false, false)
                )
                TetrominoType.S -> listOf(
                    listOf(false, true, true),
                    listOf(true, true, false),
                    listOf(false, false, false)
                )
                TetrominoType.Z -> listOf(
                    listOf(true, true, false),
                    listOf(false, true, true),
                    listOf(false, false, false)
                )
                TetrominoType.J -> listOf(
                    listOf(true, false, false),
                    listOf(true, true, true),
                    listOf(false, false, false)
                )
                TetrominoType.L -> listOf(
                    listOf(false, false, true),
                    listOf(true, true, true),
                    listOf(false, false, false)
                )
            }

            var result = base
            val rotCount = rotation % 4
            for (i in 0 until rotCount) {
                result = rotateMatrixClockwise(result)
            }
            return result
        }

        private fun rotateMatrixClockwise(matrix: List<List<Boolean>>): List<List<Boolean>> {
            val n = matrix.size
            val m = matrix[0].size
            val rotated = MutableList(m) { MutableList(n) { false } }
            for (r in 0 until n) {
                for (c in 0 until m) {
                    rotated[c][n - 1 - r] = matrix[r][c]
                }
            }
            return rotated
        }
    }
}
