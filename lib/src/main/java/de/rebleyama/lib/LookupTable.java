package de.rebleyama.lib;

/**
 * A table that contains the standard max amount of the resources.
 */
public class LookupTable {
    /**
     * Stub, you shall not create an instance of this class
     */
    private LookupTable() {
        throw new IllegalStateException("Creating an instance of this class is not allowed.");
    }
    /**
     * The standard amount of wood.
     */
    public static final int AMOUNT_WOOD = 5000;
    /**
     * The standard amount of stone.
     */
    public static final int AMOUNT_STONE = 3000;
    /**
     * The standard amount of metal.
     */
    public static final int AMOUNT_METAL = 2000;
}
