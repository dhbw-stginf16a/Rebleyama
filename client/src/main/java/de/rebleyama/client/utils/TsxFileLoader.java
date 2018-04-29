package de.rebleyama.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import de.rebleyama.lib.game.TileType;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that is responsible for loading TSX Tilemap Files and storing them in a TileSet
 */
public class TsxFileLoader {

    private TsxFileLoader() {
        throw new IllegalStateException("You should not create an instance of this class!");
    }

    /**
     * Loads a TSX file and returns a TileSet constructed out of it.
     * @param file Path to the file
     * @return A TiledMapTileSet representing the tileset in TSX.
     *         {@code null} if the TSX file is ill-formed or the file does not exist.
     */
    public static TiledMapTileSet loadFile(FileHandle file) {

        TiledMapTileSet result = new TiledMapTileSet();

        //Open file
        try {
            //start parser
            XmlReader parser = new XmlReader();
            XmlReader.Element root = parser.parse(file);

            //check if it really is a TSX file
            if (!root.getName().equals("tileset")) {
                return null;
            }

            MapProperties resultProps = result.getProperties();
            resultProps.put("name", root.getAttribute("name", null));

            // Following values are needed for calculating the TextureRegions of the tiles. If they are missing,
            // the whole thing does not make any sense. Therefore, the throwing variant of getAttribute is used.

            resultProps.put("tilewidth", root.getIntAttribute("tilewidth"));
            resultProps.put("tileheight", root.getIntAttribute("tileheight"));
            resultProps.put("tilecount", root.getIntAttribute("tilecount"));
            resultProps.put("columns", root.getIntAttribute("columns"));

            Texture tex = loadTileMapImage(file.parent(), root.getChildByName("image"));

            //Load offsets if necessary
            int xOffset = 0, yOffset = 0;

            if (root.getChildByName("tileoffset") != null) {
                xOffset = root.getChildByName("tileoffset").getIntAttribute("x", 0);
                yOffset = root.getChildByName("tileoffset").getIntAttribute("y", 0);
            }

            //Map the terraintypes to out tiletypes
            XmlReader.Element terrains = root.getChildByName("terraintypes");
            if (terrains != null) {

                Array<XmlReader.Element> terrainArray = terrains.getChildrenByName("terrain");
                Map<Integer, TileType> terrainMapping = new HashMap<Integer, TileType>(10, 0.8f);

                for (int i = 0; i < terrainArray.size; ++i) {
                    switch (terrainArray.get(i).get("name")) {
                        case "Stone":
                            terrainMapping.put(i, TileType.MOUNTAINS);
                            break;
                        case "Iron":
                            terrainMapping.put(i, TileType.IRON);
                            break;
                        case "Coal":
                            terrainMapping.put(i, TileType.COAL);
                            break;
                        case "Sand":
                            terrainMapping.put(i, TileType.DESERT);
                            break;
                        case "Shallow Water":
                            terrainMapping.put(i, TileType.SHALLOW_WATER);
                            break;
                        case "Deep Water":
                            terrainMapping.put(i, TileType.RIVER);
                            break;
                        case "Forrest": //sic!, TODO: FIX IN TILESET
                            terrainMapping.put(i, TileType.FOREST);
                            break;
                        case "Grass":
                            terrainMapping.put(i, TileType.GRASSLANDS);
                            break;
                        default:
                            Gdx.app.log("FIXME", "One of the terrains is not mapped to a TileType!");
                            terrainMapping.put(i, TileType.GRASSLANDS);
                    }
                }

                resultProps.put("terrainmapping", terrainMapping);
            }

            Array<XmlReader.Element> tiles = root.getChildrenByName("tile");
            int currentCol = 0;
            final int tileCount = (Integer) resultProps.get("tilecount"),
                    columns = (Integer) resultProps.get("columns"),
                    tileHeight = (Integer) resultProps.get("tileheight"),
                    tileWidth = (Integer) resultProps.get("tilewidth"),
                    tilesPerColumn = tileCount / columns;

            for (int i = 0; i < tiles.size && i < tileCount; ++i) {
                TextureRegion tileTex = new TextureRegion(tex, (i % tilesPerColumn) * tileWidth + xOffset, currentCol * tileHeight + yOffset, tileWidth, tileHeight);
                if (i % tilesPerColumn == tilesPerColumn - 1) ++currentCol;
                StaticTiledMapTile tile = new StaticTiledMapTile(tileTex);
                tile.setId(tiles.get(i).getIntAttribute("id", i));
                tile.getProperties().put("terrain", tiles.get(i).getAttribute("terrain", null));

                result.putTile(tile.getId(), tile);
            }
        } catch (GdxRuntimeException ex) {
            return null;
        }

        return result;
    }

    /**
     *
     * @param currentPath search path for texture
     * @param imageElem
     * @return
     */
    public static Texture loadTileMapImage(FileHandle currentPath, XmlReader.Element imageElem) {
        if (!currentPath.isDirectory() || imageElem == null || !imageElem.getName().equals("image")) return null;

        try {
            Texture tex = new Texture(currentPath.child(imageElem.getAttribute("source")));
            //Assert that succeeds if either there is no width or the width is equal to the texture's width
            assert(imageElem.getIntAttribute("width", -1) == -1 ||
                    imageElem.getIntAttribute("width") == tex.getWidth());
            //Assert that succeeds if either there is no height or the height is equal to the texture's height
            assert(imageElem.getIntAttribute("height", -1) == -1 ||
                    imageElem.getIntAttribute("height") == tex.getHeight());

            return tex;
        } catch (GdxRuntimeException ex) {
            return null;
        }
    }
}
