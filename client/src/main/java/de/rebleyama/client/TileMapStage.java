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
        Gdx.app.log("path logger",Gdx.files.internal(".").file().getAbsolutePath());
        //Texture border = new Texture(Gdx.files.internal("assets/textures/tiles/border.png"));
        for (int x = 0; x < tileMap.getWidth(); x++) {
            for (int y = 0; y < tileMap.getHeight(); y++) {
                Tile tile = tileMap.getTile(x, y);
                InteractiveTile interactiveTile = new InteractiveTile(tile, x, y);
                int tileWidth = tileMap.getTiledMap().getProperties().get("tilewidth", Integer.class);
                int tileHeight = tileMap.getTiledMap().getProperties().get("tileheight", Integer.class);
                interactiveTile.setBounds(x * tileWidth, y * tileHeight,
                        tileWidth, tileHeight);
                addActor(interactiveTile);
                EventListener eventListener = new TiledMapClickListener(interactiveTile);
                interactiveTile.addListener(eventListener);
            }
        }
    }
}