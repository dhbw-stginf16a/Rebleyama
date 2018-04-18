package de.rebleyama.lib.game;

import de.rebleyama.lib.game.resources.Metal;
import de.rebleyama.lib.game.resources.Resource;
import de.rebleyama.lib.game.resources.Stone;
import de.rebleyama.lib.game.resources.Wood;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of a player
 */
public class Player {
    /**
     * Create a new player. In the beginning, he will have 0 of all resources.
     * @param name Player Name
     */
    public Player(String name) {
        playerName = name;
        resources = new HashMap<>(10, 0.75f);

        resources.put(Metal.class, 0);
        resources.put(Stone.class, 0);
        resources.put(Wood.class, 0);
    }

    /**
     * returns how much of a specified resource a player has in inventory
     * @param resource The Class of the resource the amount is asked for
     * @return The Amount
     */
    public int getResourceAmount(Class<? extends Resource> resource) {
        Integer result = resources.get(resource);
        if (result == null) {
            throw new IllegalStateException("Unknown Resource!");
        } else
            return result;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the amount of resources to specified value.
     * @param resource The Class of the resource the amount is set
     * @param value The new resource value
     */
    public void setResourceAmount(Class<? extends Resource> resource, int value) {
        if (resources.replace(resource, value) == null) {
            throw new IllegalStateException("Unknown Resource!");
        }
    }

    private Map<Class<? extends Resource>, Integer> resources;
    private String playerName;
}
