package de.rebleyama.lib;

import java.util.EventObject;

public class ResourceDiggedEvent extends EventObject {

    /**
     * Specifies whether the resource ran out while digging.
     */
    enum Type {
        /**
         * The resource did not run out.
         */
        DIGGED,
        /**
         * The resource did run out while digging. Digging again will lead to a {@link ResourceAlreadyRanOutException}.
         */
        RANOUT,
    }

    /**
     * Creates a new ResourceDiggedEvent.
     * @param source The resource that has been digged.
     * @param resType Whether the resource ran out while digging.
     * @param amount The amount of digged resource.
     */
    public ResourceDiggedEvent(Resource source, Type resType, int amount) {
        super(source);
        this.resType = resType;
        this.amount = amount;
    }

    /**
     * Returns the amount that got digged.
     * @return The digged amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns whether the resource ran out while digging.
     * @return {@link Type}
     */
    public Type getResType() {
        return resType;
    }

    private Type resType;
    private int amount;
}
