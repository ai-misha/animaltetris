package com.example.model

data class ClearingState(
    val rowIndices: List<Int>,
    val animProgress: Float = 0f // 0f to 1f
)

data class GameState(
    val board: List<List<AnimalType?>> = List(BOARD_HEIGHT) { List(BOARD_WIDTH) { null } },
    val currentPiece: Piece? = null,
    val nextPiece: Piece = Piece.createRandom(),
    val holdPiece: Piece? = null,
    val canHold: Boolean = true,
    val score: Int = 0,
    val linesCleared: Int = 0,
    val level: Int = 1,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val isGameOver: Boolean = false,
    val isPaused: Boolean = false,
    val clearingState: ClearingState? = null,
    val isNewHighScore: Boolean = false,
    val comboCount: Int = 0
) {
    companion object {
        const val BOARD_WIDTH = 10
        const val BOARD_HEIGHT = 20
    }

    val ghostPiece: Piece?
        get() {
            val piece = currentPiece ?: return null
            var dropY = piece.y
            while (isValidPosition(piece.copy(y = dropY + 1), board)) {
                dropY++
            }
            return if (dropY != piece.y) piece.copy(y = dropY) else null
        }

    fun isValidPosition(piece: Piece, grid: List<List<AnimalType?>> = board): Boolean {
        for (r in piece.shape.indices) {
            for (c in piece.shape[r].indices) {
                if (piece.shape[r][c]) {
                    val boardX = piece.x + c
                    val boardY = piece.y + r
                    if (boardX < 0 || boardX >= BOARD_WIDTH || boardY >= BOARD_HEIGHT) {
                        return false
                    }
                    if (boardY >= 0 && grid[boardY][boardX] != null) {
                        return false
                    }
                }
            }
        }
        return true
    }
}
