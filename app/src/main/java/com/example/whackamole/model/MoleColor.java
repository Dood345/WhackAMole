package com.example.whackamole.model;

import java.util.Random;

/**
 * Represents possible mole colors and their corresponding point values.
 */
public enum MoleColor {
    RED(5),
    BLUE(3),
    GREEN(2),
    YELLOW(1),
    PURPLE(10);

    private final int points;

    MoleColor(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    /**
     * Returns a random MoleColor.
     */
    public static MoleColor randomColor() {
        MoleColor[] values = values();
        return values[new Random().nextInt(values.length)];
    }
}
