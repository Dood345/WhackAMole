package com.example.whackamole.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.whackamole.model.GameConfig;
import com.example.whackamole.model.MoleContainer;
import com.example.whackamole.repository.fake.FakeGameRepository;
import com.example.whackamole.util.fake.FakeScheduler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

public class GameViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FakeGameRepository fakeGameRepository;
    private FakeScheduler fakeScheduler;
    private GameViewModel gameViewModel;

    @Before
    public void setup() {
        fakeGameRepository = new FakeGameRepository(0);
        fakeScheduler = new FakeScheduler();
        gameViewModel = new GameViewModel(fakeGameRepository, fakeScheduler,
                new GameConfig(1, 5, 1000, 100, 50));
    }

    /**
     * Test initial state of the ViewModel.
     * <p>
     * Strategy:
     * - Verify score starts at 0.
     * - Verify high score matches repository.
     * - Verify gameOver is false.
     * - Verify a visible mole is set with a valid ID.
     */
    @Test
    public void testInitialState() {
        assertEquals(0, Objects.requireNonNull(gameViewModel.getScore().getValue()).intValue());
        assertEquals(
                Objects.requireNonNull(fakeGameRepository.getHighScore().getValue()).intValue(),
                Objects.requireNonNull(gameViewModel.getHighScore().getValue()).intValue());
        assertFalse(Objects.requireNonNull(gameViewModel.getGameOver().getValue()));
        assertTrue(Objects.requireNonNull(gameViewModel.getMoles().getValue()).getVisibleId() >= 0);
    }

    /**
     * Test hitting the correct mole.
     * <p>
     * Strategy:
     * - Get the currently visible mole ID.
     * - Call hitMole with this ID.
     * - Verify score increments by 1.
     * - Verify high score is updated in both LiveData and repository.
     */
    @Test
    public void testHitMoleIncrementsScoreAndHighScore() {
        int visibleId = Objects.requireNonNull(gameViewModel.getMoles().getValue()).getVisibleId();
        int expectedPoints = gameViewModel.getMoles().getValue().getMoles().get(visibleId)
                .getColor().getPoints();
        
        gameViewModel.hitMole(visibleId);

        // Score should be incremented
        assertEquals(expectedPoints,
                Objects.requireNonNull(gameViewModel.getScore().getValue()).intValue());

        // High score should be updated
        assertEquals(expectedPoints,
                Objects.requireNonNull(gameViewModel.getHighScore().getValue()).intValue());
        assertEquals(expectedPoints,
                Objects.requireNonNull(fakeGameRepository.getHighScore().getValue()).intValue());
    }

    /**
     * Test hitting the wrong mole.
     * <p>
     * Strategy:
     * - Get the currently visible mole ID.
     * - Tap a different mole ID.
     * - Verify that score and high score do not change.
     */
    @Test
    public void testHitWrongMoleDoesNotIncrementsScoreAndHighScore() {
        MoleContainer currentMoles = Objects.requireNonNull(gameViewModel.getMoles().getValue());
        int visibleId = currentMoles.getVisibleId();
        int wrongId = (visibleId + 1) % currentMoles.getMoles().size();

        gameViewModel.hitMole(wrongId);

        assertEquals(0, Objects.requireNonNull(gameViewModel.getScore().getValue()).intValue());
        assertEquals(0, Objects.requireNonNull(gameViewModel.getHighScore().getValue()).intValue());
    }

    /**
     * Test resetting the game after game over.
     * <p>
     * Strategy:
     * - Simulate game over by calling spawnMole enough times to reach MAX_MISSES.
     * - Verify gameOver flag is true.
     * - Call resetGame.
     * - Verify score resets to 0.
     * - Verify gameOver flag is false.
     * - Verify a visible mole is set with a valid ID.
     */
    @Test
    public void testResetGameAfterGameOver() {
        // Trigger game over since test max_misses passed is 1
        fakeScheduler.runNext();
        assertTrue(Objects.requireNonNull(gameViewModel.getGameOver().getValue()));

        // Reset game
        gameViewModel.resetGame();
        assertEquals(0, Objects.requireNonNull(gameViewModel.getScore().getValue()).intValue());
        assertFalse(Objects.requireNonNull(gameViewModel.getGameOver().getValue()));
        assertTrue(Objects.requireNonNull(gameViewModel.getMoles().getValue()).getVisibleId() >= 0);
    }
}
