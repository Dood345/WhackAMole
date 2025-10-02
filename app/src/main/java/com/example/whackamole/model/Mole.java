package com.example.whackamole.model;

/**
 * Immutable data class representing a Mole in the game.
 *
 * TODO: Color and Points per mole.
 */
public final class Mole {

    private final int id;
    private final boolean visible;

    /**
     * Constructor
     *
     * @param id      unique identifier for the mole
     * @param visible whether the mole is currently visible
     */
    public Mole(int id, boolean visible) {
        this.id = id;
        this.visible = visible;
    }

    /**
     * Get the mole ID
     *
     * @return unique ID of the mole
     */
    public int getId() {
        return id;
    }

    /**
     * Check if the mole is visible
     *
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    @Override
    public String toString() {
        return "Mole{" + "id=" + id + ", visible=" + visible + '}';
    }
}
