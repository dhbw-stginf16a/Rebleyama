package de.rebleyama.lib.gamestate;

import de.rebleyama.lib.game.Player;
import de.rebleyama.lib.game.TileMap;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the final class providing the game state object.
 */
public class GameState {
    private TileMap tileMap;
    private Map<Byte, Player> players;
    /**
     * Creates a new GameState object
     */
    public GameState() {
        this.tileMap = new TileMap(512, 512);
        this.players = new HashMap<>();
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
     * Adds a new player to the game
     * @param clientId the id of the connecting client, received by the handshake
     * @param playerName the nickname of the player.
     */
    public void addPlayer(byte clientId, String playerName) {
        this.players.put(clientId, new Player(playerName));
    }

    /**
     * Removes a player from the gamestate
     * @param clientId which disconnected
     */
    public void deletePlayer(byte clientId) {
        this.players.remove(clientId);
    }

    /**
     * Returns a single player object
     * @param clientId id of the client to update
     * @return the player object of the given id
     */
    public Player getPlayer(byte clientId) {
        return this.players.get(clientId);
    }

    /**
     * Returns the whole player map for easier batch processing
     * @return Map of player data
     */
    public Map<Byte, Player> getPlayers() {
        return this.players;
    }

    /**
     * Get the tile map from the game state
     * @return The tile map
     */
    public TileMap getTileMap() {
        return tileMap;
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
