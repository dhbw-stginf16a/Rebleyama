package de.rebleyama.lib;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The representation of a resource. You can however not directly create an instance of this class. Please create a child class that sets the max amount.
 */
public class Resource {
    /**
     * Creates a new Resource.
     * @param amount The maximum amount that can be digged.
     */
    protected Resource(int amount) {
        resourceAmount = amount;
        listeners = new LinkedList<>();
    }

    /**
     * Adds an EventListener that will be informed about digging.
     * If listener is null, an exception is thrown and nothing happens.
     * @param listener The listener to add.
     */
    public void addEventListener(ResourceDiggedEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener must not be null!");
        }
        listeners.add(listener);
    }

    /**
     * Removes an EventListener that will no longer be informed about digging.
     * If listener is null, an exception is thrown and nothing happens.
     * @param listener The listener to remove.
     */
    public void removeEventListener(ResourceDiggedEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("The listener must not be null!");
        }
        listeners.remove(listener);
    }

    /**
     * Returns all listeners currently listening on this resource.
     * @return An array of all listening listeners, or an empty array if no one listens.
     */
    public ResourceDiggedEventListener[] getListeners() {
        return listeners.toArray(new ResourceDiggedEventListener[0]);
    }

    /**
     * decrease the amount of the resource. Will invoke the event listeners.
     * @param amount specifies how much should be digged. If {@code amount > resourceAmount}, only the current resource amount will be digged and the type of the Event will be set to the {@link ResourceDiggedEvent.Type} RANOUT.
     * @throws ResourceAlreadyRanOutException Thrown when the resource already ran out before digging.
     * @throws IllegalArgumentException Thrown when the given amount is negative.
     */
    public void dig(int amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("It is not allowed to dig a negative amount of resources.");
        }

        if (resourceAmount <= 0) {
            throw new ResourceAlreadyRanOutException("The resource amount is already zero.");
        }

        ResourceDiggedEvent event;

        if (amount > resourceAmount) {
            amount = resourceAmount;
            resourceAmount = 0;
            event = new ResourceDiggedEvent(this, ResourceDiggedEvent.Type.RANOUT, amount);
        }
        else {
            resourceAmount -= amount;
            event = new ResourceDiggedEvent(this, ResourceDiggedEvent.Type.DIGGED, amount);
        }
        callListeners(event);
    }

    /**
     * calls all listeners using given event
     * @param event Event to call the listeners with.
     */
    private void callListeners(ResourceDiggedEvent event) {
        for(ResourceDiggedEventListener i : listeners) {
            i.resourceDigged(event);
        }
    }

    //Listeners that will be informed if the resource amount decreases.
    private Collection<ResourceDiggedEventListener> listeners;

    private int resourceAmount;
}
