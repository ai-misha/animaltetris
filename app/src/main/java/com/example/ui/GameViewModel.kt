package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffects
import com.example.data.AppDatabase
import com.example.data.ScoreRecord
import com.example.data.ScoreRepository
import com.example.model.AnimalType
import com.example.model.ClearingState
import com.example.model.Difficulty
import com.example.model.GameState
import com.example.model.Piece
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScoreRepository = ScoreRepository(
        AppDatabase.getDatabase(application).scoreDao()
    )

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true) // Default to dark theme
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _showLeaderboard = MutableStateFlow(false)
    val showLeaderboard: StateFlow<Boolean> = _showLeaderboard.asStateFlow()

    private val _showDifficultySelect = MutableStateFlow(false)
    val showDifficultySelect: StateFlow<Boolean> = _showDifficultySelect.asStateFlow()

    private val _leaderboardDifficulty = MutableStateFlow(Difficulty.MEDIUM)
    val leaderboardDifficulty: StateFlow<Difficulty> = _leaderboardDifficulty.asStateFlow()

    private val _hasSavedScore = MutableStateFlow(false)
    val hasSavedScore: StateFlow<Boolean> = _hasSavedScore.asStateFlow()

    val topScores: StateFlow<List<ScoreRecord>> = _leaderboardDifficulty
        .flatMapLatest { diff -> repository.getTopScores(diff.name) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var gameLoopJob: Job? = null
    private var isClearingAnimationRunning = false

    init {
        startGame()
    }

    fun startGame(difficulty: Difficulty = _gameState.value.difficulty) {
        gameLoopJob?.cancel()
        isClearingAnimationRunning = false
        _hasSavedScore.value = false

        val firstPiece = Piece.createRandom()
        val next = Piece.createRandom()

        _gameState.value = GameState(
            difficulty = difficulty,
            currentPiece = firstPiece,
            nextPiece = next,
            isGameOver = false,
            isPaused = false
        )

        _leaderboardDifficulty.value = difficulty
        launchGameLoop()
    }

    private fun launchGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = viewModelScope.launch {
            while (isActive) {
                val state = _gameState.value
                if (!state.isGameOver && !state.isPaused && !isClearingAnimationRunning) {
                    val dropInterval = state.difficulty.getSpeedForLevel(state.level)
                    delay(dropInterval)
                    // Check again after delay
                    if (!_gameState.value.isGameOver && !_gameState.value.isPaused && !isClearingAnimationRunning) {
                        stepGravity()
                    }
                } else {
                    delay(100)
                }
            }
        }
    }

    private fun stepGravity() {
        val state = _gameState.value
        val piece = state.currentPiece ?: return

        val movedPiece = piece.copy(y = piece.y + 1)
        if (state.isValidPosition(movedPiece)) {
            _gameState.update { it.copy(currentPiece = movedPiece) }
        } else {
            // Lock piece onto the board
            lockPieceAndCheckLines()
        }
    }

    fun moveLeft() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning) return
        val piece = state.currentPiece ?: return

        val moved = piece.copy(x = piece.x - 1)
        if (state.isValidPosition(moved)) {
            _gameState.update { it.copy(currentPiece = moved) }
            SoundEffects.playMove()
        }
    }

    fun moveRight() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning) return
        val piece = state.currentPiece ?: return

        val moved = piece.copy(x = piece.x + 1)
        if (state.isValidPosition(moved)) {
            _gameState.update { it.copy(currentPiece = moved) }
            SoundEffects.playMove()
        }
    }

    fun rotatePiece() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning) return
        val piece = state.currentPiece ?: return

        val rotated = piece.rotatedClockwise()

        // Try standard rotation, then wall kicks
        val kickOffsets = listOf(
            0 to 0,
            -1 to 0,
            1 to 0,
            -2 to 0,
            2 to 0,
            0 to -1
        )

        for ((dx, dy) in kickOffsets) {
            val candidate = rotated.copy(x = rotated.x + dx, y = rotated.y + dy)
            if (state.isValidPosition(candidate)) {
                _gameState.update { it.copy(currentPiece = candidate) }
                SoundEffects.playRotate()
                return
            }
        }
    }

    fun softDrop() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning) return
        val piece = state.currentPiece ?: return

        val moved = piece.copy(y = piece.y + 1)
        if (state.isValidPosition(moved)) {
            _gameState.update {
                it.copy(
                    currentPiece = moved,
                    score = it.score + 1
                )
            }
            SoundEffects.playMove()
        } else {
            lockPieceAndCheckLines()
        }
    }

    fun hardDrop() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning) return
        val piece = state.currentPiece ?: return

        var dropY = piece.y
        while (state.isValidPosition(piece.copy(y = dropY + 1))) {
            dropY++
        }

        val droppedCells = dropY - piece.y
        val droppedPiece = piece.copy(y = dropY)

        _gameState.update {
            it.copy(
                currentPiece = droppedPiece,
                score = it.score + (droppedCells * 2)
            )
        }
        SoundEffects.playDrop()
        lockPieceAndCheckLines()
    }

    fun holdPiece() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused || isClearingAnimationRunning || !state.canHold) return
        val current = state.currentPiece ?: return

        SoundEffects.playHold()

        val currentType = current.type
        val held = state.holdPiece

        if (held == null) {
            val next = state.nextPiece
            val newNext = Piece.createRandom()
            val newPiece = Piece.create(next.type)
            _gameState.update {
                it.copy(
                    holdPiece = Piece.create(currentType),
                    currentPiece = newPiece,
                    nextPiece = newNext,
                    canHold = false
                )
            }
        } else {
            val newPiece = Piece.create(held.type)
            _gameState.update {
                it.copy(
                    holdPiece = Piece.create(currentType),
                    currentPiece = newPiece,
                    canHold = false
                )
            }
        }
    }

    private fun lockPieceAndCheckLines() {
        val state = _gameState.value
        val piece = state.currentPiece ?: return

        // Place piece onto grid
        val mutableBoard = state.board.map { it.toMutableList() }.toMutableList()
        for (r in piece.shape.indices) {
            for (c in piece.shape[r].indices) {
                if (piece.shape[r][c]) {
                    val bx = piece.x + c
                    val by = piece.y + r
                    if (by in 0 until GameState.BOARD_HEIGHT && bx in 0 until GameState.BOARD_WIDTH) {
                        mutableBoard[by][bx] = piece.animal
                    }
                }
            }
        }

        // Check for full rows
        val fullRowIndices = mutableListOf<Int>()
        for (r in 0 until GameState.BOARD_HEIGHT) {
            if (mutableBoard[r].all { it != null }) {
                fullRowIndices.add(r)
            }
        }

        if (fullRowIndices.isNotEmpty()) {
            // Trigger joyful jumping animation for cleared lines!
            triggerLineClearJumpAnimation(mutableBoard.map { it.toList() }, fullRowIndices)
        } else {
            // Spawn next piece
            spawnNextPiece(mutableBoard.map { it.toList() }, state.linesCleared, state.score)
        }
    }

    private fun triggerLineClearJumpAnimation(
        boardWithLockedPiece: List<List<AnimalType?>>,
        rowIndices: List<Int>
    ) {
        isClearingAnimationRunning = true
        SoundEffects.playLineClearJump()

        _gameState.update {
            it.copy(
                board = boardWithLockedPiece,
                currentPiece = null,
                clearingState = ClearingState(rowIndices = rowIndices, animProgress = 0f)
            )
        }

        viewModelScope.launch {
            val animDurationMs = 600L
            val frames = 30
            val delayPerFrame = animDurationMs / frames

            for (f in 1..frames) {
                delay(delayPerFrame)
                val progress = f.toFloat() / frames.toFloat()
                _gameState.update {
                    it.copy(
                        clearingState = it.clearingState?.copy(animProgress = progress)
                    )
                }
            }

            // Animation done: remove cleared rows from board
            val updatedBoard = boardWithLockedPiece.filterIndexed { index, _ ->
                !rowIndices.contains(index)
            }.toMutableList()

            // Prepend new empty rows on top
            val clearedCount = rowIndices.size
            repeat(clearedCount) {
                updatedBoard.add(0, List(GameState.BOARD_WIDTH) { null })
            }

            val state = _gameState.value
            val linesMultiplier = when (clearedCount) {
                1 -> 100
                2 -> 300
                3 -> 500
                else -> 800
            }
            val addedScore = (linesMultiplier * state.level * state.difficulty.scoreMultiplier).toInt()
            val newLines = state.linesCleared + clearedCount
            val newLevel = 1 + (newLines / 10)

            _gameState.update {
                it.copy(
                    board = updatedBoard,
                    clearingState = null,
                    linesCleared = newLines,
                    level = newLevel,
                    score = it.score + addedScore
                )
            }

            isClearingAnimationRunning = false
            spawnNextPiece(updatedBoard, newLines, state.score + addedScore)
        }
    }

    private fun spawnNextPiece(
        board: List<List<AnimalType?>>,
        lines: Int,
        score: Int
    ) {
        val state = _gameState.value
        val next = state.nextPiece
        val newPiece = Piece.create(next.type)
        val futureNext = Piece.createRandom()

        if (!state.isValidPosition(newPiece, board)) {
            // Game Over!
            SoundEffects.playGameOver()
            _gameState.update {
                it.copy(
                    board = board,
                    currentPiece = null,
                    isGameOver = true
                )
            }
        } else {
            _gameState.update {
                it.copy(
                    board = board,
                    currentPiece = newPiece,
                    nextPiece = futureNext,
                    canHold = true
                )
            }
        }
    }

    fun togglePause() {
        val state = _gameState.value
        if (state.isGameOver) return
        _gameState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun pauseGame() {
        val state = _gameState.value
        if (state.isGameOver || state.isPaused) return
        _gameState.update { it.copy(isPaused = true) }
    }

    fun toggleMute() {
        val newMuted = !_isMuted.value
        _isMuted.value = newMuted
        SoundEffects.isMuted = newMuted
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun openLeaderboard(diff: Difficulty? = null) {
        if (diff != null) {
            _leaderboardDifficulty.value = diff
        } else {
            _leaderboardDifficulty.value = _gameState.value.difficulty
        }
        _showLeaderboard.value = true
    }

    fun closeLeaderboard() {
        _showLeaderboard.value = false
    }

    fun setLeaderboardDifficulty(difficulty: Difficulty) {
        _leaderboardDifficulty.value = difficulty
    }

    fun openDifficultySelect() {
        _showDifficultySelect.value = true
    }

    fun closeDifficultySelect() {
        _showDifficultySelect.value = false
    }

    fun selectDifficultyAndRestart(difficulty: Difficulty) {
        _showDifficultySelect.value = false
        startGame(difficulty)
    }

    fun saveScore(playerName: String) {
        val state = _gameState.value
        if (_hasSavedScore.value || state.score == 0) return

        viewModelScope.launch {
            val record = ScoreRecord(
                difficulty = state.difficulty.name,
                playerName = playerName,
                score = state.score,
                linesCleared = state.linesCleared,
                level = state.level
            )
            repository.saveScore(record)
            _hasSavedScore.value = true
        }
    }

    fun clearDifficultyScores(difficulty: Difficulty) {
        viewModelScope.launch {
            repository.clearScores(difficulty.name)
        }
    }
}
