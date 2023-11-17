/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel.ui;

/**
 * This class describes a result of a command execution.
 * This class was taken from the solution for Task number 5 of SS 2023.
 * @author Programmieren-Team
 */
public class Result {
    private final ResultType type;
    private final String message;

    /**
     * Constructs a new Result without message.
     *
     * @param type the type of the result.
     */
    public Result(final ResultType type) {
        this(type, null);
    }

    /**
     * Constructs a new Result with message.
     *
     * @param type the type of the result.
     * @param message message to carry
     */
    public Result(ResultType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Returns the type of result.
     *
     * @return the type of result.
     */
    public ResultType getType() {
        return this.type;
    }

    /**
     * Returns the carried message of the result or {@code null} if there is none.
     *
     * @return the message or {@code null}
     */
    public String getMessage() {
        return this.message;
    }

}
