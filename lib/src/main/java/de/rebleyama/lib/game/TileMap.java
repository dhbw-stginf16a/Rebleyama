package de.rebleyama.lib.game;

import de.rebleyama.lib.utils.TileReplacedEvent;
import de.rebleyama.lib.utils.TileReplacedListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A representation of a map of tiles.
 */
public class TileMap {
    /**
     * Create a new TileMap skeleton. The tiles are all null.
     * @param width width of map
     * @param height height of map
     */
    public TileMap(int width, int height) {
        tileMap = new Tile[width][];
        for (int i = 0; i < width; ++i) {
            tileMap[i] = new Tile[height];
        }

        listeners = new ArrayList<>();
    }

    /**
     * Fills all tiles with Tile instances of the TileType type. If overrideAll is set,
     * EVERY Tile in the Map will be overridden.
     * @param type TileType of the new tiles
     * @param overrideAll if true, will override all tiles. If false, only null will be overridden.
     */
    public void fillNullTiles(TileType type, boolean overrideAll) {
        for (int x = 0; x < tileMap.length; ++x) {
            for (int y = 0; y < tileMap[x].length; ++y) {
                if (tileMap[x][y] == null || overrideAll) {
                    tileMap[x][y] = new Tile(type);
                }
            }
        }
    }

    /**
     * Sets the tile at position (x,y) to a tile, overriding an existing Tile
     * @param x x coordinate
     * @param y y coordinate
     * @param tile the Tile
     */
    public void setTile(int x, int y, Tile tile) {
        Tile temp = tileMap[x][y];
        tileMap[x][y] = tile;
        callListeners(new TileReplacedEvent(this, x, y, temp, tile));
    }

    /**
     * Returns the tile at specified position
     * @param x x coordinate
     * @param y y coordinate
     * @return the Tile at position (x,y), can be null if not set
     */
    public Tile getTile(int x, int y) {
        return tileMap[x][y];
    }

    public int getWidth() {
        return tileMap.length;
    }

    public int getHeight() {
        return tileMap[0].length;
    }

    /**
     *
     * @param listener
     */
    public void addEventListener(TileReplacedListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(TileReplacedListener listener) {
        listeners.remove(listener);
    }

    private void callListeners(TileReplacedEvent event) {
        for (TileReplacedListener listener : listeners) {
            listener.tileChanged(event);
        }
    }

    private Tile[][] tileMap;
    private Collection<TileReplacedListener> listeners;
}
