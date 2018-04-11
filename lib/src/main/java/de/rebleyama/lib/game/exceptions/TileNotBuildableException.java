package de.rebleyama.lib.game.exceptions;

/**
 * Thrown when user tried to place a TilePlacable on a unbuildable Tile.
 */
public class TileNotBuildableException extends RuntimeException {
    public TileNotBuildableException(String message) {
        super(message);
    }
}
