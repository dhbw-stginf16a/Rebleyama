package de.rebleyama.lib.game.exceptions;

import de.rebleyama.lib.game.resources.Resource;

/**
 * Thrown when a {@link Resource} that already ran out is collected.
 */
public class ResourceAlreadyRanOutException extends RuntimeException {
    public ResourceAlreadyRanOutException(String message) {
        super(message);
    }
}
