package de.rebleyama.client;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.rebleyama.client.utils.DataInfusedTileMap;
import de.rebleyama.lib.game.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TileMapStage extends Stage {

    private DataInfusedTileMap tileMap;

    public TileMapStage(DataInfusedTileMap tileMap, FitViewport worldViewport) {
        this.tileMap = tileMap;
        createActors(tileMap);
    }

    private void createActors(DataInfusedTileMap tileMap) {
        Texture border = new Texture(Gdx.files.absolute(
                "C:\\Users\\Jan-Robin Aumann\\OneDrive\\Dokumente\\Duales Studium Angewandte Informatik\\STGINF16A\\Software-Engineering\\Rebleyama\\client\\assets\\textures\\tiles\\border.png"));
        for (int x = 0; x < tileMap.getWidth(); x++) {
            for (int y = 0; y < tileMap.getHeight(); y++) {
                Tile tile = tileMap.getTile(x, y);
                InteractiveTile interactiveTile = new InteractiveTile(tile, border, x, y);
                interactiveTile.setBounds(x * DataInfusedTileMap.TILE_WIDTH, y * DataInfusedTileMap.TILE_HEIGHT,
                        DataInfusedTileMap.TILE_WIDTH, DataInfusedTileMap.TILE_HEIGHT);
                addActor(interactiveTile);
                EventListener eventListener = new TiledMapClickListener(interactiveTile);
                interactiveTile.addListener(eventListener);
            }
        }
    }
}