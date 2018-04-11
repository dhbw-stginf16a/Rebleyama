package de.rebleyama.lib.game;

import de.rebleyama.lib.game.buildings.TilePlacable;
import de.rebleyama.lib.game.exceptions.TileNotBuildableException;
import de.rebleyama.lib.game.resources.Resource;

/**
 * Represents a tile of the map.
 */
public class Tile {
    private TileType type;
    private Resource res;
    private TilePlacable elementOnTile;

    /**
     * Constructs a tile.
     * @param type Type of the tile.
     */
    public Tile(TileType type) {
        this.type = type;
        res = type.getNewAssociatedResource();
    }

    /**
     * Transforms the tile to a new type. If a false preserveResource,
     * the Tile will get a new instance of the resource associated to the new TileType.
     * @param type The new type of the tile.
     * @param preserveResource If set to true, the former resource will not be replaced.
     *                        THAT COULD LEAD TO CONSISTENCY ERRORS, HANDLE WITH CARE!
     */
    public void setType(TileType type, boolean preserveResource) {
        this.type = type;
        if (!preserveResource) {
            res = type.getNewAssociatedResource();
        }
    }

    /**
     * Transforms the tile to a new type without preserving the old resource.
     * @param type The new type of the tile.
     */
    public void setType(TileType type) {
        setType(type, false);
    }

    /**
     * Returns the current TileType.
     * @return The current TileType.
     */
    public TileType getType() {
        return type;
    }

    /**
     * Returns whether you are allowed to place a TilePlacable on the Tile.
     * @return true if allowed, false if not.
     */
    public boolean isBuildable() {
        return type.isBuildable();
    }

    /**
     * Places a TilePlacable on this tile.
     * @param element The thing that should be placed.
     * @throws TileNotBuildableException Thrown when the TileType forbids placing something on the tile.
     */
    public void placeOnTile(TilePlacable element) {
        if (!isBuildable()) {
            throw new TileNotBuildableException("This tile is unbuildable; placing something here is not allowed.");
        }

        elementOnTile = element;
    }

    public TilePlacable getTilePlacable() {
        return elementOnTile;
    }

    /**
     * Removes the current TilePlacable from the Tile. Noop if there is no TilePlacable.
     */
    public void deleteBuildableFromTile() {
        elementOnTile = null;
    }

    /**
     * Removes the current Resource from the Tile. Noop if there is no Resource on the Tile.
     */
    public void deleteResource() {
        res = null;
    }

    /**
     * Returns the current Resource.
     * @return The current Resource.
     */
    public Resource getResource() {
        return res;
    }
}
