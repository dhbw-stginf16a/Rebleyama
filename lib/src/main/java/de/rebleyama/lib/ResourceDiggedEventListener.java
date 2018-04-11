package de.rebleyama.lib;

import java.util.EventListener;

/**
 *  The listener interface for listening for resource digging.
 */
@FunctionalInterface
public interface ResourceDiggedEventListener extends EventListener {
    /**
     * Invoked when a {@link Resource} has been digged (=had its amount decreased).
     * @param event The event that got invoked.
     */
    void resourceDigged(ResourceDiggedEvent event);
}
