/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel.ui;

import java.util.Objects;

/**
 * This class represents a basic command that can be executed by the user.
 * This class was taken from the solution for Task number 5 of SS 2023.
 * @author Programmieren-Team
 */
public abstract class Command {

    /**
     * The command handler.
     */
    protected final CommandHandler commandHandler;
    private final String commandName;

    /**
     * Constructs a new Command.
     * @param commandName    the name of the command
     * @param commandHandler the command handler
     */
    protected Command(final String commandName, final CommandHandler commandHandler) {
        this.commandName = Objects.requireNonNull(commandName);
        this.commandHandler = Objects.requireNonNull(commandHandler);
    }

    /**
     * Returns the name of the command.
     * @return the name of the command
     */
    public final String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    public abstract void execute(String[] commandArguments);

}
