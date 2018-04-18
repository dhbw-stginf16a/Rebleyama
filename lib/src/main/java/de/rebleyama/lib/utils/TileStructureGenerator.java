package de.rebleyama.lib.utils;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.game.TileMap;
import de.rebleyama.lib.game.TileType;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that can be used to construct our Data Structure from a TiledMap
 */
public class TileStructureGenerator {
    private TileStructureGenerator() {
        throw new IllegalStateException("You are not allowed to construct an instance of this file.");
    }

    /*
            TODO provide some kind of metadata/config that contains a list of tiles
             in our game and a mapping to our terrains,
             do not hardcode tile set IDs
         */

    /**
     * ID of Coal Tiles
     */
    public static final int TILESET_CODE_COAL = 1;
    /**
     * ID of Grass Tiles
     */
    public static final int TILESET_CODE_GRASSLANDS = 2;
    /**
     * ID of Iron Tiles
     */
    public static final int TILESET_CODE_IRON = 3;
    /**
     * ID of Stone Tiles
     */
    public static final int TILESET_CODE_STONE = 4;
    /**
     * ID of Water Tiles
     */
    public static final int TILESET_CODE_WATER = 5;

    /**
     * Generate a TileMap using a TiledMap.
     * @deprecated Only for debugging and testing purposes.
     *             Will be removed after generation of TileMaps is no longer needed
     *             because maps are no longer loaded with TMX but with auto generation.
     * @param tiledMap The tiledMap that we should get the tile information from.
     * @return A list of lists of Tiles. Please note that the coordinate system between
     *         libGDX and Tiled differs: (0;0) is the lower left corner in libGDX, while in
     *         Tiled, it is the upper left corner. This method will use libGDX indexes.
     */
    @Deprecated
    public static TileMap generateTiles(TiledMap tiledMap) {
        //Get the layer
        final TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        //check if layer is valid
        if (tileLayer == null) {
            throw new IllegalArgumentException("The specified TiledMap does not contain an appropriate layer!");
        }

        //iterate through the map
        final int height = tileLayer.getHeight();
        final int width = tileLayer.getWidth();

        //generate temp object
        TileMap result = new TileMap(height, width);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; y++) {
                TileType tileType;
                //get ID of the tile of the associated cell
                switch(tileLayer.getCell(x, y).getTile().getId()) {
                    case TILESET_CODE_COAL:
                        //unimplemented
                    case TILESET_CODE_IRON:
                        //unimplemented
                    case TILESET_CODE_STONE:
                        tileType = TileType.MOUNTAINS;
                        break;
                    case TILESET_CODE_GRASSLANDS:
                        tileType = TileType.GRASSLANDS;
                        break;
                    case TILESET_CODE_WATER:
                        tileType = TileType.RIVER;
                        break;
                    default:
                        throw new UnsupportedOperationException("This tile type is not supported!");
                }
                result.setTile(x, y, new Tile(tileType));
            }
        }
        return result;
    }
}
