package de.rebleyama.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.utils.Disposable;
import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.game.TileMap;
import de.rebleyama.lib.game.TileType;

//TODO Maybe better extend TiledMap instead?
/**
 * A TileMap that contains a TiledMap for rendering.
 */
public class DataInfusedTileMap extends TileMap implements Disposable {

    //TODO provide some kind of metadata/config that contains a list of tiles in our game and a mapping to our terrains
    public static final String MAP_ORIENTATION = "orthogonal";

    private static Texture tileMapImage;
    private static TiledMapTileSet tileSet;

    /**
     * Loads
     */
    private void loadTileSet() {
        if (tileSet != null) {
            return; //Already loaded TileSet
        }

        tileSet = new RebleyamaTmxFileLoader().loadTSX(Gdx.files.internal("assets/textures/tiles/tilemap.tsx"));
    }

    private void fillTiledMap() {

        //make sure that the TileMap is completely filled with default values
        fillNullTiles(TileType.GRASSLANDS, false);

        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                setCellAccordingToTileType(x, y, getTile(x, y).getType());
            }
        }
    }

    private void setCellAccordingToTileType(int x, int y, TileType tileType) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        int tileSetCode = -1;
        for (TiledMapTile tile : tileSet) {
            if ((TileType) tile.getProperties().get("rebleyamaTerrain") == tileType) {
                tileSetCode = tile.getId();
                break;
            }
        }
        cell.setTile(tileSet.getTile(tileSetCode));
        ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(x, y, cell);
    }

    /**
     * Create a DataInfusedTileMap. It will be filled with Grassland by default.
     * @param width Width of the new map
     * @param height Height of the new map
     */
    public DataInfusedTileMap(int width, int height) {
        super(width, height);

        loadTileSet();

        //Create the TiledMap
        tiledMap = new TiledMap();

        //Set basic properties
        MapProperties props = tiledMap.getProperties();

        props.put("width", width);
        props.put("height", height);
        props.put("tileWidth", tileSet.getProperties().get("tilewidth"));
        props.put("tileHeight", tileSet.getProperties().get("tileheight"));
        props.put("mapOrientation", MAP_ORIENTATION);

        //Set the TileSet to our tileset

        tiledMap.getTileSets().addTileSet(tileSet);

        tiledMap.getLayers().add(new TiledMapTileLayer(width, height, (Integer)tileSet.getProperties().get("tilewidth"),
                (Integer) tileSet.getProperties().get("tileheight")));
        tiledMap.getLayers().get(0).setName("Ground");

        fillTiledMap();
    }

    /**
     * Returns a representation of the TileMap.
     * @return A TiledMap that represents the current TileMap.
     * DO NOT CHANGE THE CELLS IN IT! THAT WILL LEAD TO INCONSISTENCIES!
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * Cleanup the internal TiledMap.
     */
    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }

        if (tileMapImage != null) {
            tileMapImage.dispose();
            tileMapImage = null;
        }
    }

    /**
     * Sets the tile at position (x,y) to a tile, overriding an existing Tile
     * and corrects the TiledMap
     * @param x x coordinate
     * @param y y coordinate
     * @param tile the Tile
     */
    @Override
    public void setTile(int x, int y, Tile tile) {
        super.setTile(x, y, tile);

        setCellAccordingToTileType(x, y, tile.getType());
    }

    private TiledMap tiledMap;
}
