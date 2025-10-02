package com.example.whackamole.repository.fake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whackamole.repository.GameRepository;

/**
 * FakeGameRepository is an in-memory implementation of GameRepository
 * for testing purposes. Does not persist data to disk.
 */
public class FakeGameRepository implements GameRepository {

    private final MutableLiveData<Integer> highScore;

    /**
     * Constructor
     *
     * @param highScore initial high score
     */
    public FakeGameRepository(int highScore) {
        this.highScore = new MutableLiveData<>(highScore);
    }

    /**
     * Get the current high score as LiveData.
     *
     * @return LiveData of current high score
     */
    @Override
    public LiveData<Integer> getHighScore() {
        return highScore;
    }

    /**
     * Update the high score.
     *
     * @param score new high score
     */
    @Override
    public void saveHighScore(int score) {
        highScore.setValue(score);
    }
}
