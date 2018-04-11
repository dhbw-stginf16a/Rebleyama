package de.rebleyama.lib.game.resources;

import java.util.EventListener;

/**
 *  The listener interface for listening for resource collecting.
 */
@FunctionalInterface
public interface ResourceCollectedEventListener extends EventListener {
    /**
     * Invoked when a {@link Resource} has been collected (=had its amount decreased).
     * @param event The event that got invoked.
     */
    void resourceCollected(ResourceCollectedEvent event);
}
