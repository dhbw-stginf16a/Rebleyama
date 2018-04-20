package de.rebleyama.lib.gamestate;

/**
 * This is the final class providing the game state object.
 */
public class GameState {

    /**
     * Creates a new GameState object
     */
    public GameState() {
        // I have no idea yet.
    }

    /** Apply a received gameStateUpdate to the current gamestate
     *
     * @param gameStateUpdate the received or generated update
     * @return Success of the update
     */
    public boolean applyUpdate(GameStateUpdate gameStateUpdate) {
        // We will see
        return true;
    }

    /**
     * Returns a identifiable hash of the current game state.
     * @return Hash of the current game state
     */
    public String getHash() {
        // TODO: Evaluate efficiency of hashing methods
        return this.toString();
    }
}
