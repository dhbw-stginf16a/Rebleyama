package de.rebleyama.client;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TiledMapActor extends Actor {

    public TiledMap tiledMap;

    public TiledMapTileLayer tiledLayer;

    public TiledMapTileLayer.Cell cell;

    public TiledMapActor(TiledMap tiledMap, TiledMapTileLayer tiledLayer, TiledMapTileLayer.Cell cell) {
        this.tiledMap = tiledMap;
        this.tiledLayer = tiledLayer;
        this.cell = cell;
    }

}