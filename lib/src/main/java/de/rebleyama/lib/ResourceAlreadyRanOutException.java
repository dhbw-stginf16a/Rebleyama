package de.rebleyama.lib;

/**
 * Thrown when a {@link Resource} that already ran out is digged.
 */
public class ResourceAlreadyRanOutException extends RuntimeException {
    public ResourceAlreadyRanOutException(String message) {
        super(message);
    }
}
