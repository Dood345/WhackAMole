package com.example.whackamole.viewmodel;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.whackamole.repository.GameRepository;
import com.example.whackamole.repository.fake.FakeGameRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private GameRepository fakeRepository;
    private MainViewModel viewModel;

    @Before
    public void setUp() {
        fakeRepository = new FakeGameRepository(5);
        viewModel = new MainViewModel(fakeRepository);
    }

    @Test
    public void testInitialHighScoreIsLoadedFromRepository() {
        assertEquals(5,
                Objects.requireNonNull(fakeRepository.getHighScore().getValue()).intValue());
        assertEquals(5, Objects.requireNonNull(viewModel.getHighScore().getValue()).intValue());
    }

    @Test
    public void testsaveHighScoreUpdatesValueInViewModel() {
        fakeRepository.saveHighScore(10);
        assertEquals(10, Objects.requireNonNull(viewModel.getHighScore().getValue()).intValue());
    }

    @Test
    public void clearHighScore_resetsRepoAndLiveDataToZero() {

        // Clear it
        viewModel.clearHighScore();
        assertEquals(0,
                Objects.requireNonNull(fakeRepository.getHighScore().getValue()).intValue());
        assertEquals(0, Objects.requireNonNull(viewModel.getHighScore().getValue()).intValue());
    }
}
