package de.rebleyama.lib.game.resources;

import de.rebleyama.lib.game.exceptions.ResourceAlreadyRanOutException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The representation of a resource. You can however not directly create an instance of this class. Please create a child class that sets the max amount.
 */
public abstract class Resource {
    /**
     * Creates a new Resource.
     * @param amount The maximum amount that can be collected.
     */
    Resource(int amount) {
        resourceAmount = amount;
        listeners = new LinkedList<>();
    }

    /**
     * Adds an EventListener that will be informed about collecting.
     * If listener is null, an exception is thrown and nothing happens.
     * @param listener The listener to add.
     */
    public void addEventListener(ResourceCollectedEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener must not be null!");
        }
        listeners.add(listener);
    }

    /**
     * Removes an EventListener that should no longer be informed about collecting.
     * If listener is null, an exception is thrown and nothing happens.
     * @param listener The listener to remove.
     */
    public void removeEventListener(ResourceCollectedEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener must not be null!");
        }
        listeners.remove(listener);
    }

    /**
     * Returns all listeners currently listening on this resource.
     * @return An array of all listening listeners, or an empty array if no one listens.
     */
    public ResourceCollectedEventListener[] getListeners() {
        return listeners.toArray(new ResourceCollectedEventListener[0]);
    }

    /**
     * Decrease the amount of the resource. Will invoke the event listeners.
     * @param amount specifies how much should be collected. If {@code amount > resourceAmount}, only the current resource amount will be collected and the type of the Event will be set to the {@link ResourceCollectedEvent.Type} {@code RANOUT}.
     * @throws ResourceAlreadyRanOutException Thrown when the resource already ran out before collecting.
     * @throws IllegalArgumentException Thrown when the given amount is negative.
     */
    public void collect(int amount) {

        //check for negative amount
        if (amount < 0) {
            throw new IllegalArgumentException("It is not allowed to collect a negative amount of resources.");
        }

        //check if resource already ran out
        if (resourceAmount <= 0) {
            throw new ResourceAlreadyRanOutException("The resource amount is already zero.");
        }

        ResourceCollectedEvent.Type eventType;

        //tried to collect more than available; will only harvest rest
        if (amount > resourceAmount) {
            amount = resourceAmount;
            resourceAmount = 0;
            eventType = ResourceCollectedEvent.Type.RANOUT;
        }
        //everything alright
        else {
            resourceAmount -= amount;
            eventType = ResourceCollectedEvent.Type.COLLECTED;
        }
        callListeners(new ResourceCollectedEvent(this, eventType, amount));
    }

    /**
     * calls all listeners using given event
     * @param event Event to call the listeners with.
     */
    private void callListeners(ResourceCollectedEvent event) {
        for(ResourceCollectedEventListener i : listeners) {
            i.resourceCollected(event);
        }
    }

    //Listeners that will be informed if the resource amount decreases.
    private Collection<ResourceCollectedEventListener> listeners;

    private int resourceAmount;
}
