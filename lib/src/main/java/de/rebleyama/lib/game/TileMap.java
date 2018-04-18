package de.rebleyama.lib.game;

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
        for (Tile[] column : tileMap) {
            column = new Tile[height];
        }
    }

    /**
     * Fills all tiles with Tile instances of the TileType type. If overrideAll is set,
     * EVERY Tile in the Map will be overridden.
     * @param type TileType of the new tiles
     * @param overrideAll if true, will override all tiles. If false, only null will be overridden.
     */
    public void fillNullTiles(TileType type, boolean overrideAll) {
        for (Tile[] column : tileMap) {
            for (Tile tile : column) {
                if (tile == null || overrideAll) {
                    tile = new Tile(type);
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
        tileMap[x][y] = tile;
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

    private Tile[][] tileMap;
}
