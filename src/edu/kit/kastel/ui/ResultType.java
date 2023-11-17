/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel.ui;


/**
 * The type of Result of a execution.
 * This class is taken from the solution for the task 5 of SS 2023.
 * @author Programmieren-Team
 */
public enum ResultType {

    /**
     * The execution did not end with success.
     */
    FAILURE() {
        @Override
        public <T> void printResult(final String formattedMessage, T... args) {
            System.err.printf("ERROR: " + formattedMessage + NEW_LINE_SYMBOL, args);
        }
    },

    /**
     * The execution did end with success.
     */
    SUCCESS() {
        @Override
        public <T> void printResult(final String formattedMessage, T... args) {
            System.out.printf(formattedMessage + NEW_LINE_SYMBOL, args);
        }
    };

    private static final String NEW_LINE_SYMBOL = "%n";

    /**
     * Prints the result of the execution.
     * @param formattedMessage  the formatted message
     * @param args              the arguments
     * @param <T>               the type of the arguments
     */
    public abstract <T> void printResult(String formattedMessage, T... args);

}
