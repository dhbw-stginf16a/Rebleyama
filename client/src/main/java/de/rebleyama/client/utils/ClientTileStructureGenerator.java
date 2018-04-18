package de.rebleyama.client.utils;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.rebleyama.lib.utils.TileStructureGenerator;

/**
 * A class that can be used to construct our Data Structure from a TiledMap
 */
public class ClientTileStructureGenerator extends TileStructureGenerator {
    private ClientTileStructureGenerator() {
        throw new IllegalStateException("You are not allowed to construct an instance of this file.");
    }

    /**
     * Generate a DataInfusedTileMap using a TiledMap.
     * @deprecated Only for debugging and testing purposes.
     *             Will be removed after generation of TileMaps is no longer needed
     *             because maps are no longer loaded with TMX but with auto generation.
     * @param tiledMap The tiledMap that we should get the tile information from.
     * @return A list of lists of Tiles. Please note that the coordinate system between
     *         libGDX and Tiled differs: (0;0) is the lower left corner in libGDX, while in
     *         Tiled, it is the upper left corner. This method will use libGDX indexes.
     */
    @Deprecated
    public static DataInfusedTileMap generateTiles(TiledMap tiledMap) {
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
        DataInfusedTileMap result = new DataInfusedTileMap(width, height);

        TileStructureGenerator.putTilesInMap(result, tiledMap);

        return result;
    }
}
