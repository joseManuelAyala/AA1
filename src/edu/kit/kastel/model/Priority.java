package edu.kit.kastel.model;

/**
 * Represents the priority for a task.
 * Each priority has a corresponding value and order.
 * The order helps to sort the task.
 * @author ucxug
 * @version 1.0
 */

public enum Priority {

    /**
     * Represents the High priority for a task.
     * It has the order 4.
     */
    HI("HI", 4),
    /**
     * Represents the Medium priority for a task.
     * It has the order 3.
     */
    MD("MD", 3),
    /**
     * Represents the Low priority for a task.
     * It has the order 2.
     */
    LO("LO", 2),

    /**
     * Represents the default value priority for a task.
     * It means not defined.
     * It has the order 1.
     */
    ND("", 1);

    private final String value;
    private final int priorityOrder;

    /**
     * Constructor for creating a Priority enum value.
     * @param priorityValue the Priority value.
     * @param order The priority order.
     */

    Priority(final String priorityValue, final int order) {
        this.value = priorityValue;
        this.priorityOrder = order;
    }

    /**
     * Gets the order of the priority.
     * @return The priority order.
     */

    public int getPriorityOrder() {
        return this.priorityOrder;
    }

    /**
     * Gets the value of the priority.
     * @return the priority value.
     */
    public String getValue() {
        return this.value;
    }
}
