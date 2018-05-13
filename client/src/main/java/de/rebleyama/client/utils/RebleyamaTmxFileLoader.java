package de.rebleyama.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import de.rebleyama.lib.game.TileType;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RebleyamaTmxFileLoader extends TmxMapLoader {
    @Override
    protected void loadTileSet(TiledMap map, XmlReader.Element element, FileHandle tmxFile, ImageResolver imageResolver) {
        super.loadTileSet(map, element, tmxFile, imageResolver);

        if (element.getName().equals("tileset")) {
            String source = element.getAttribute("source", null);
            if (source != null) {
                FileHandle tsx = getRelativeFileHandle(tmxFile, source);
                element = xml.parse(tsx);
            }

            XmlReader.Element terrains = element.getChildByName("terrain");

            if (terrains != null) {
                //Generate a terrain to TileType mapping
                Map<Integer, TileType> terrainMapping = readTerrainIdToTileTypeMappingFromTSX(element);
                TiledMapTileSet tileSet = map.getTileSets().getTileSet(element.get("name", null));
                tileSet.getProperties().put("rebleyamaTerrainMapping", terrainMapping);

                //Give every tile a TileType
                tileSet.forEach((tile) -> {
                    MapProperties tileProp = tile.getProperties();
                    String terrainProp = tileProp.get("terrain", null);
                    if (terrainProp != null) {
                        String[] splitTiledTerrain = terrainProp.split(",");
                        Map<Integer, Integer> countInts = new HashMap<>(6, 0.8f);

                        for (String terr : splitTiledTerrain) {
                            int terrToInt = Integer.parseInt(terr);
                            if (countInts.get(terrToInt) != null) {
                                countInts.replace(terrToInt, countInts.get(terrToInt),
                                        countInts.get(terrToInt) + 1);
                            } else {
                                countInts.put(terrToInt, 1);
                            }
                        }

                        int terrainId = Collections.max(countInts.entrySet(),
                                Comparator.comparingInt(Map.Entry<Integer, Integer>::getValue)).getKey();
                        tileProp.put("rebleyamaTerrain", terrainMapping.get(terrainId));
                    }
                });
            }
        }
    }

    public TiledMapTileSet loadTSX(FileHandle file) {
        XmlReader.Element root = xml.parse(file);
        TiledMap map = new TiledMap();

        ObjectMap<String, FileHandle> tileSetTextureFileMap = loadImageFromTSX(root, file);
        ObjectMap<String, Texture> tileSetTextureMap = new ObjectMap<>();
        for (ObjectMap.Entry<String, FileHandle> entry : tileSetTextureFileMap) {
            tileSetTextureMap.put(entry.key, new Texture(entry.value));
        }

        ImageResolver.DirectImageResolver imageResolver = new ImageResolver.DirectImageResolver(tileSetTextureMap);

        loadTileSet(map, root, file, imageResolver);

        return map.getTileSets().getTileSet(0);

    }

    protected ObjectMap<String, FileHandle> loadImageFromTSX(XmlReader.Element root, FileHandle tsxFile) {
        ObjectMap<String, FileHandle> result = new ObjectMap<>();
        if (root.getName().equals("tileset")) {
            XmlReader.Element image = root.getChildByName("image");
            if (image != null) {
                String imageSource = image.getAttribute("source", null);
                if (imageSource != null) {
                    result.put(imageSource, getRelativeFileHandle(tsxFile, imageSource));
                }
            }
        }

        return result;
    }

    /**
     * Reads the terrain mapping out of a TSX Tileset File.
     * @param rootElem The root of the TSX XML DOM ({@code <tileset>})
     * @return A map from terrain IDs to TileTypes. {@code null} if a problem occured.
     */
    private Map<Integer, TileType> readTerrainIdToTileTypeMappingFromTSX(XmlReader.Element rootElem) {
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
