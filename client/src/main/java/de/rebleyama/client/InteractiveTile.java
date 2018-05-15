package de.rebleyama.client;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.rebleyama.lib.game.Tile;

import com.badlogic.gdx.graphics.Texture;

public class InteractiveTile extends Image {

    public Tile tile;
    public int x_coordinate;
    public int y_coordinate;

    public InteractiveTile(Tile tile, Texture border, int x, int y) {
        super(border);
        this.tile = tile;
        x_coordinate = x;
        y_coordinate = y;
    }

}