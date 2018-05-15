package de.rebleyama.client;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class TiledMapClickListener extends ClickListener {

    private InteractiveTile interactiveTile;

    public TiledMapClickListener(InteractiveTile interactiveTile) {
        this.interactiveTile = interactiveTile;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("Actor Click Logger",
                interactiveTile.tile + " has been clicked. It's a " + interactiveTile.tile.getType()
                        + " tile! Its coordinates (X/Y) are: " + interactiveTile.x_coordinate + "/"
                        + interactiveTile.y_coordinate);
    }
}