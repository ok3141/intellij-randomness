package com.fwdekker.randomness.integer;


/**
 * Contains settings for generating random integers.
 */
final class IntegerSettings {
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 1000;

    /**
     * The minimum value to be generated, inclusive.
     */
    private static int minValue = DEFAULT_MIN_VALUE;
    /**
     * The maximum value to be generated, inclusive.
     */
    private static int maxValue = DEFAULT_MAX_VALUE;


    /**
     * Private to prevent instantiation.
     */
    private IntegerSettings() {
    }


    /**
     * Returns the minimum value to be generated, inclusive.
     *
     * @return the minimum value to be generated, inclusive
     */
    static int getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimum value to be generated.
     *
     * @param minValue the minimum value to be generated, inclusive
     */
    static void setMinValue(final int minValue) {
        IntegerSettings.minValue = minValue;
    }

    /**
     * Returns the maximum value to be generated, inclusive.
     *
     * @return the maximum value to be generated, inclusive
     */
    static int getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximum value to be generated.
     *
     * @param maxValue the maximum value to be generated, inclusive
     */
    static void setMaxValue(final int maxValue) {
        IntegerSettings.maxValue = maxValue;
    }
}