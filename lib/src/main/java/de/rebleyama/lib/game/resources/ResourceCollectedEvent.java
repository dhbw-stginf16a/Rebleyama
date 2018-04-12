package de.rebleyama.lib.game.resources;

import de.rebleyama.lib.game.exceptions.ResourceAlreadyRanOutException;

import java.util.EventObject;

/**
 * This event is invoked to {@link ResourceCollectedEventListener} when resources got collected.
 */
public class ResourceCollectedEvent extends EventObject {

    /**
     * Specifies whether the resource ran out while collecting.
     */
    enum Type {
        /**
         * The resource did not run out.
         */
        COLLECTED,
        /**
         * The resource did run out while collecting.
         * Collecting again will lead to a {@link ResourceAlreadyRanOutException}.
         */
        RANOUT,
    }

    /**
     * Creates a new ResourceCollectedEvent.
     * @param source The resource that has been collected.
     * @param resType Whether the resource ran out while collecting.
     * @param amount The amount of collected resource.
     */
    public ResourceCollectedEvent(Resource source, Type resType, int amount) {
        super(source);
        this.resType = resType;
        this.amount = amount;
    }

    /**
     * Returns the amount that got collected.
     * @return The collected amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns whether the resource ran out while collecting.
     * @return {@link Type}
     */
    public Type getResType() {
        return resType;
    }

    private Type resType;
    private int amount;
}
