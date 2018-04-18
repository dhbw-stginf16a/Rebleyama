package de.rebleyama.lib.utils;

import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.game.TileMap;

import java.util.EventObject;

public class TileReplacedEvent extends EventObject {

    public TileReplacedEvent(TileMap source, int x, int y, Tile oldTile, Tile newTile) {
        super(source);

        _x = x;
        _y = y;

        _oldTile = oldTile;
        _newTile = newTile;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public Tile getOldTile() {
        return _oldTile;
    }

    public Tile getNewTile() {
        return _newTile;
    }

    private int _x, _y;
    private Tile _oldTile, _newTile;
}
