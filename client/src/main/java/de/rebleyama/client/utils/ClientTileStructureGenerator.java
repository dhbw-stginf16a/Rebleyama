package de.rebleyama.client.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.game.TileMap;
import de.rebleyama.lib.game.TileType;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that can be used to construct our Data Structure from a TiledMap
 */
public class ClientTileStructureGenerator {
    private ClientTileStructureGenerator() {
        throw new IllegalStateException("You are not allowed to construct an instance of this file.");
    }

    /**
     * Reads the terrain mapping out of a TSX Tileset File.
     * @param rootElem The root of the TSX XML DOM ({@code <tileset>})
     * @return A map from terrain IDs to TileTypes. {@code null} if a problem occured.
     */
    public static Map<Integer, TileType> readTerrainIdToTileTypeMappingFromTSX(XmlReader.Element rootElem) {
        //check if it really is a TSX file
        if (!rootElem.getName().equals("tileset")) {
            return null;
        }

        XmlReader.Element terrains = rootElem.getChildByName("terraintypes");
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

            return terrainMapping;
        }

        return null;
    }


}
