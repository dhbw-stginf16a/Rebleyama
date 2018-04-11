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
    },
    /**
     * Forest, provides Wood
     */
    FOREST {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return new Wood(); }
    },
    /**
     * Mountain, provides Stone
     */
    MOUNTAINS {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return new Stone(); }
    },
    /**
     * River
     */
    RIVER {
        @Override public boolean isBuildable() { return false; }
        @Override public Resource getNewAssociatedResource() { return null; }
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
}
