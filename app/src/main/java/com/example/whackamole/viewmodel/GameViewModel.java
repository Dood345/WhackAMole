package com.example.whackamole.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whackamole.model.GameConfig;
import com.example.whackamole.model.MoleContainer;
import com.example.whackamole.repository.GameRepository;
import com.example.whackamole.util.Scheduler;

import java.util.Objects;
import java.util.Random;

/**
 * GameViewModel manages the core logic for the Whack-a-Mole game.
 * It handles mole spawning, scoring, misses, game-over state,
 * and high score persistence via a GameRepository.
 * <p>
 * LiveData exposed by this ViewModel:
 * - LiveData<MoleContainer> getMoles() – current immutable MoleContainer
 * - LiveData<Integer> getScore() – current player score
 * - LiveData<Integer> getHighScore() – high score (persisted after destruction of object)
 * - LiveData<Boolean> getGameOver() – game-over state
 * - LiveData<Integer> getMisses() - current number of misses
 * <p>
 * Public Functions:
 * - void hitMole(int moleId) – register a mole tap
 * - void resetGame() – restart the game on the same screen
 */
public class GameViewModel extends ViewModel {

    private final Random random = new Random();

    private final GameConfig gameConfig;
    private final GameRepository gameRepository;
    private final Scheduler scheduler;
    private final Runnable spawnRunnable = this::spawnMole;
    private final LiveData<Integer> highScore;
    private final MutableLiveData<Integer> score;
    private final MutableLiveData<Boolean> gameOver;
    private final MutableLiveData<MoleContainer> moles;
    private final MutableLiveData<Integer> misses;
    private long currentInterval;

    public GameViewModel(GameRepository gameRepository, Scheduler scheduler) {
        this(gameRepository, scheduler, GameConfig.DEFAULT);
    }

    public GameViewModel(GameRepository gameRepository, Scheduler scheduler,
                         GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.gameRepository = gameRepository;
        this.scheduler = scheduler;

        this.highScore = gameRepository.getHighScore();

        this.score = new MutableLiveData<>(0);
        this.gameOver = new MutableLiveData<>(false);
        this.moles = new MutableLiveData<>(
                new MoleContainer(gameConfig.getNumMoles(),
                        random.nextInt(gameConfig.getNumMoles())));
        this.misses = new MutableLiveData<>(0);

        this.currentInterval = gameConfig.getInitialInterval();

        scheduler.postDelayed(spawnRunnable, gameConfig.getInitialInterval());
    }

    /**
     * Spawns a mole at a new random position. If this runs, it counts as a miss.
     */
    private void spawnMole() {
        boolean isGameOver = Objects.requireNonNull(gameOver.getValue());
        if (isGameOver) {
            throw new IllegalStateException("spawnMole should never be called after game over.");
        }

        MoleContainer currentMoles = Objects.requireNonNull(moles.getValue());

        // Increment miss because previous mole was not hit
        int currentMisses = Objects.requireNonNull(misses.getValue());
        misses.setValue(currentMisses + 1);

        if (misses.getValue() >= gameConfig.getMaxMisses()) {
            gameOver.setValue(true);
            scheduler.removeCallbacks(spawnRunnable);
            return;
        }

        // Pick new mole ID avoiding previous one
        int newVisibleId = random.nextInt(gameConfig.getNumMoles() - 1);
        newVisibleId = newVisibleId >= currentMoles.getVisibleId() ? newVisibleId + 1 : newVisibleId;

        moles.setValue(new MoleContainer(gameConfig.getNumMoles(), newVisibleId));

        // Speed up interval
        currentInterval = Math.max(gameConfig.getMinInterval(),
                currentInterval - gameConfig.getIntervalDecrement());

        // Schedule next spawn
        scheduler.removeCallbacks(spawnRunnable);
        scheduler.postDelayed(spawnRunnable, currentInterval);
    }

    /**
     * Called when the user taps a mole. Updates score, high score, and schedules next spawn.
     */
    public void hitMole(int moleId) {
        boolean isGameOver = Objects.requireNonNull(gameOver.getValue());
        if (isGameOver) {
            throw new IllegalStateException("hitMole should not be called after game over.");
        }

        MoleContainer currentMoles = Objects.requireNonNull(moles.getValue());
        int currentScore = Objects.requireNonNull(score.getValue());
        int currentHighScore = Objects.requireNonNull(highScore.getValue());

        if (currentMoles.getVisibleId() != moleId) {
            return;
        }

        // Increment score
        int newScore = currentScore + currentMoles.getMoles().get(moleId).getColor().getPoints();
        score.setValue(newScore);

        // Update high score if needed
        if (newScore > currentHighScore) {
            gameRepository.saveHighScore(newScore);
        }

        // Pick new mole ID
        int newVisibleId = random.nextInt(gameConfig.getNumMoles() - 1);
        newVisibleId = newVisibleId >= currentMoles.getVisibleId() ? newVisibleId + 1 : newVisibleId;
        moles.setValue(new MoleContainer(gameConfig.getNumMoles(), newVisibleId));

        // Speed up interval
        currentInterval = Math.max(gameConfig.getMinInterval(),
                currentInterval - gameConfig.getIntervalDecrement());

        // Reset spawn timer
        scheduler.removeCallbacks(spawnRunnable);
        scheduler.postDelayed(spawnRunnable, currentInterval);
    }

    /**
     * Resets the game and starts a new session. Only callable after game over.
     */
    public void resetGame() {
        boolean isGameOver = Objects.requireNonNull(gameOver.getValue());
        if (!isGameOver) {
            throw new IllegalStateException("resetGame should only be called after game over.");
        }

        misses.setValue(0);
        currentInterval = gameConfig.getInitialInterval();

        score.setValue(0);
        gameOver.setValue(false);
        moles.setValue(new MoleContainer(gameConfig.getNumMoles(), random.nextInt(
                gameConfig.getNumMoles())));

        scheduler.postDelayed(spawnRunnable, currentInterval);
    }

    // LiveData getters
    public LiveData<MoleContainer> getMoles() {
        return moles;
    }

    public LiveData<Integer> getScore() {
        return score;
    }

    public LiveData<Integer> getHighScore() {
        return highScore;
    }

    public LiveData<Boolean> getGameOver() {
        return gameOver;
    }

    public LiveData<Integer> getMisses() {
        return misses;
    }

    /**
     * Ends the game and stops the loop.
     */
    @Override
    protected void onCleared() {
        scheduler.removeCallbacksAndMessages(null);
    }
}
