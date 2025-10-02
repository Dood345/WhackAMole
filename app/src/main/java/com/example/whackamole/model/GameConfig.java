package com.example.whackamole.model;

/**
 * Holds immutable configuration values for the Whack-a-Mole game.
 */
public final class GameConfig {

    private final int maxMisses;
    private final int numMoles;
    private final long initialInterval;
    private final long minInterval;
    private final long intervalDecrement;

    // Default configuration
    public static final GameConfig DEFAULT = new GameConfig(
            5,     // maxMisses
            9,     // numMoles
            2000,  // initialInterval in ms
            500,   // minInterval in ms
            100    // intervalDecrement per spawn
    );

    public GameConfig(int maxMisses, int numMoles, long initialInterval, long minInterval,
                      long intervalDecrement) {
        this.maxMisses = maxMisses;
        this.numMoles = numMoles;
        this.initialInterval = initialInterval;
        this.minInterval = minInterval;
        this.intervalDecrement = intervalDecrement;
    }

    public int getMaxMisses() {
        return maxMisses;
    }

    public int getNumMoles() {
        return numMoles;
    }

    public long getInitialInterval() {
        return initialInterval;
    }

    public long getMinInterval() {
        return minInterval;
    }

    public long getIntervalDecrement() {
        return intervalDecrement;
    }
}
