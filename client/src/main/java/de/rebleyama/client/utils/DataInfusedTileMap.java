package de.rebleyama.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Disposable;
import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.game.TileMap;
import de.rebleyama.lib.game.TileType;
import de.rebleyama.lib.utils.TileStructureGenerator;

//TODO Maybe better extend TiledMap instead?
/**
 * A TileMap that contains a TiledMap for rendering.
 */
public class DataInfusedTileMap extends TileMap implements Disposable {

    //TODO provide some kind of metadata/config that contains a list of tiles in our game and a mapping to our terrains
    public static final int TILE_WIDTH = 40;
    public static final int TILE_HEIGHT = 40;
    public static final int TILE_COUNT = 5;
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

        tileSet = new TiledMapTileSet();

        if (tileMapImage == null) {
            tileMapImage = new Texture(Gdx.files.absolute("C:\\Users\\Jan-Robin Aumann\\OneDrive\\Dokumente\\Duales Studium Angewandte Informatik\\STGINF16A\\Software-Engineering\\Rebleyama\\client\\assets\\textures\\tiles\\tilemap.png"));
        }

        if (tileMapImage.getWidth() < TILE_WIDTH) {
            tileMapImage.dispose();
            throw new IllegalArgumentException("TileMap Texture too small!");
        }


        /*
            TODO provide some kind of metadata/config that contains a
             list of tiles in our game and a mapping to our terrains,
             current implementation depends on the TileMap Texture not to change order of tiles!
         */
        int currentID = 1;
        for (int line = 0; line * TILE_HEIGHT < tileMapImage.getHeight(); ++line) {
            if (currentID >= TILE_COUNT) {
                break;
            }
            for (int col = 0; col * TILE_WIDTH < tileMapImage.getWidth(); ++col) {
                if (currentID < TILE_COUNT) {
                    TiledMapTile tile = new StaticTiledMapTile(
                            new TextureRegion(tileMapImage, col * TILE_WIDTH, line * TILE_HEIGHT,
                                    TILE_WIDTH, TILE_HEIGHT)
                    );
                    tile.setId(currentID);
                    tileSet.putTile(currentID, tile);
                    ++currentID;
                }
                else {
                    break;
                }
            }
        }
    }

    private void fillTiledMap() {

        //make sure that the TileMap is completely filled with default values
        fillNullTiles(TileType.GRASSLANDS, false);

        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                setCellAccordingToTileType(x, y, getTile(x, y).getType(), (TiledMapTileLayer) tiledMap.getLayers().get(0));
            }
        }
    }

    private void setCellAccordingToTileType(int x, int y, TileType tileType, TiledMapTileLayer layer) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        int tileSetCode;
        switch(tileType) {
            case GRASSLANDS:
                tileSetCode = TileStructureGenerator.TILESET_CODE_GRASSLANDS;
                break;

            case SHALLOW_WATER:
                //TODO!
                tileSetCode = TileStructureGenerator.TILESET_CODE_WATER;
                break;

            case RIVER:
                tileSetCode = TileStructureGenerator.TILESET_CODE_WATER;
                break;

            case MOUNTAINS:
                tileSetCode = TileStructureGenerator.TILESET_CODE_STONE;
                break;

            case FOREST:
                //TODO!
                tileSetCode = TileStructureGenerator.TILESET_CODE_GRASSLANDS;
                break;

            case DESERT:
                //TODO!
                tileSetCode = TileStructureGenerator.TILESET_CODE_GRASSLANDS;
                break;

            default:
                throw new IllegalStateException("TileType " + getTile(x, y).getType().toString() +" unknown!");
        }

        cell.setTile(tileSet.getTile(tileSetCode));
        layer.setCell(x, y, cell);
    }

    /**
     * Create a DataInfusedTileMap. It will be filled with Grassland by default.
     * @param width Width of the new map
     * @param height Height of the new map
     */
    public DataInfusedTileMap(int width, int height) {
        super(width, height);
        //Create the TiledMap
        tiledMap = new TiledMap();

        //Set basic properties
        MapProperties props = tiledMap.getProperties();

        props.put("width", width);
        props.put("height", height);
        props.put("tileWidth", TILE_WIDTH);
        props.put("tileHeight", TILE_HEIGHT);
        props.put("mapOrientation", MAP_ORIENTATION);

        //Set the TileSet to our tileset
        loadTileSet();
        tiledMap.getTileSets().addTileSet(tileSet);

        tiledMap.getLayers().add(new TiledMapTileLayer(width, height, TILE_WIDTH, TILE_HEIGHT));
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

        setCellAccordingToTileType(x, y, tile.getType(), (TiledMapTileLayer) tiledMap.getLayers().get(0));
    }

    private TiledMap tiledMap;
}
