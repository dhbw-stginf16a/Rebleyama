package de.rebleyama.client;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.rebleyama.lib.game.Tile;

import com.badlogic.gdx.graphics.Texture;

public class InteractiveTile extends Actor {

    public Tile tile;
    public int x_coordinate;
    public int y_coordinate;

    public InteractiveTile(Tile tile, int x, int y) {
        super();
        this.tile = tile;
        x_coordinate = x;
        y_coordinate = y;
    }

}