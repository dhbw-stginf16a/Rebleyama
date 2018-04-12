package de.rebleyama.lib.utils;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import de.rebleyama.lib.game.Tile;
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

    public static final int TILESET_CODE_COAL = 0;
    public static final int TILESET_CODE_GRASSLANDS = 1;
    public static final int TILESET_CODE_IRON = 2;
    public static final int TILESET_CODE_STONE = 3;
    public static final int TILESET_CODE_WATER = 4;

    /**
     * Generate a list of lists of Tiles.
     * @param tiledMap The tiledMap that we should get the tile information from.
     * @return A list of lists of Tiles. The index of the outer index is the row,
     *         the index of the inner index the column.
     */
    public static List<List<Tile>> generateTiles(TiledMap tiledMap) {
        //Get the layer
        final TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        //check if layer is valid
        if (tileLayer == null) {
            throw new IllegalArgumentException("The specified TiledMap does not contain an appropriate layer!");
        }

        //generate temp object
        List<List<Tile>> result = new ArrayList<>();

        //iterate through the map
        final int height = tileLayer.getHeight();
        final int width = tileLayer.getWidth();

        for (int x = 0; x < height; ++x) {
            List<Tile> row = new ArrayList<>();
            for (int y = 0; y < width; y++) {
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
                row.add(new Tile(tileType));
            }
            //TODO: assert or better always check?
            assert row.size() == width;
            result.add(row);
        }
        //TODO: assert or better always check?
        assert result.size() == height;
        return result;
    }
}
