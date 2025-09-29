package com.example.whackamole

/**
 * Model class that manages game state and logic for Whack-a-Mole.
 *
 * This class handles all game data including score, timing, and mole positions.
 * It does not interact with UI components directly.
 *
 * @author JT
 */
class GameModel {
    /** Current score in the active game, with koltrain we don't need a getter */
    var score: Int = 0
        private set

    /** Highest score achieved across all games, with koltrain we don't need a getter */
    var highScore: Int = 0
        private set

    /** Whether a game is currently in progress */
    var isGameActive: Boolean = false
        private set

    /** Remaining time in current game (seconds), with koltrain we don't need a getter */
    var gameTime: Int = 0
        private set

    /** Total duration of a game (seconds) */
    val gameDuration: Int = 30

    /** List of all holes and their current state */
    private val holes: MutableList<Hole> = mutableListOf()

    /** Total number of holes in the game grid */
    val numberOfHoles: Int = 9

    init {
        // Initialize holes
        for (i in 0 until numberOfHoles) {
            holes.add(Hole(id = i, hasMole = false))
        }
    }

    /**
     * Data class representing a single hole in the game.
     *
     * @property id Unique identifier for this hole
     * @property hasMole Whether a mole is currently in this hole
     */
    data class Hole(val id: Int, var hasMole: Boolean)

    /**
     * Initializes a new game session.
     * Resets score to 0, sets game time to duration, and activates game state.
     */
    fun startGame() {
        // TODO: Implement game start logic
    }

    /**
     * Ends the current game session.
     * Updates high score if current score is higher and deactivates game state.
     */
    fun endGame() {
        // TODO: Implement game end logic
    }

    /**
     * Resets the current game state without ending the game.
     * Sets score back to 0 and clears all moles.
     */
    fun resetGame() {
        // TODO: Implement game reset logic
    }

    /**
     * Clears the saved high score, setting it to 0.
     */
    fun clearHighScore() {
        // TODO: Implement clear high score logic
    }

    /**
     * Randomly spawns a mole in an available hole.
     *
     * @return The ID of the hole where the mole spawned, or -1 if no holes available
     */
    fun spawnMole(): Int {
        // TODO: Implement mole spawning logic
        return -1
    }

    /**
     * Removes the mole from a specific hole.
     *
     * @param holeId The ID of the hole to clear
     */
    fun removeMole(holeId: Int) {
        // TODO: Implement mole removal logic
    }

    /**
     * Handles a successful mole whack, updating score and removing the mole.
     *
     * @param holeId The ID of the hole that was clicked
     * @return true if a mole was successfully whacked, false if hole was empty
     */
    fun whackMole(holeId: Int): Boolean {
        // TODO: Implement mole whacking logic
        return false
    }

    /**
     * Increments the current score by the standard point value.
     */
    fun incrementScore() {
        // TODO: Implement score increment logic
    }

    /**
     * Updates high score if current score exceeds it.
     */
    fun updateHighScore() {
        // TODO: Implement update high score logic
    }

    /**
     * Checks if a game is currently active.
     *
     * @return true if game is active, false otherwise
     */
    fun isActive(): Boolean {
        // TODO: Implement is active logic
        return false
    }

    /**
     * Gets the current state of all holes.
     *
     * @return List of all hole states
     */
    fun getHoles(): List<Hole> {
        // TODO: Implement get holes logic
        return emptyList()
    }
}
