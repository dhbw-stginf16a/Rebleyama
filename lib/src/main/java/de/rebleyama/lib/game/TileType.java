package de.rebleyama.lib.game;

import de.rebleyama.lib.game.resources.Resource;
import de.rebleyama.lib.game.resources.Stone;
import de.rebleyama.lib.game.resources.Wood;

/**
 * Represents the type information about a tile.
 */
public enum TileType {
    /**
     * Grassland
     */
    GRASSLANDS {
        @Override public boolean isBuildable() { return true; }
        @Override public Resource getNewAssociatedResource() { return null; }
        @Override public boolean isWalkable() { return true; }
    },
    /**
     * Forest, provides Wood
     */
    FOREST {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return new Wood(); }
        @Override public boolean isWalkable() { return true; }
    },
    /**
     * Mountain, provides Stone
     */
    MOUNTAINS {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return new Stone(); }
        @Override public boolean isWalkable() { return false; }
    },
    /**
     * River
     */
    RIVER {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return null; }
        @Override public boolean isWalkable() { return false; }
    };

    /**
     * On a tile, you can either place a building or not.
     * A tile that allows you to build buildings on it is called buildable.
     * @return {@code true} if buildable, {@code false} if not buildable.
     */
    public abstract boolean isBuildable();

    /**
     * A tile can contain a resource. The TileType is a factory for its associated resource.
     * You can call this method to get back that resource.
     * @return A new instance of the full {@link Resource} that belongs on that TileType.
     *         {@code null} if a Tile of that {@link TileType} never has a {@link Resource}.
     */
    public abstract Resource getNewAssociatedResource();

    /**
     * Not every tile is walkable by a unit. This method returns whether a tile of that Type can be entered.
     * @return {@code true} if walkable, {@code false} if not.
     */
    public abstract boolean isWalkable();
}
