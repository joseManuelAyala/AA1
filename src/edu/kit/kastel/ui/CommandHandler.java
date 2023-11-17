/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel.ui;
import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.ui.commands.AddCommand;
import edu.kit.kastel.ui.commands.AddListCommand;
import edu.kit.kastel.ui.commands.AssignCommand;
import edu.kit.kastel.ui.commands.BeforeCommand;
import edu.kit.kastel.ui.commands.BetweenCommand;
import edu.kit.kastel.ui.commands.ChangePriorityCommand;
import edu.kit.kastel.ui.commands.ChangeDateCommand;
import edu.kit.kastel.ui.commands.DeleteCommand;
import edu.kit.kastel.ui.commands.DuplicatesCommand;
import edu.kit.kastel.ui.commands.FindCommand;
import edu.kit.kastel.ui.commands.ListCommand;
import edu.kit.kastel.ui.commands.QuitCommand;
import edu.kit.kastel.ui.commands.RestoreCommand;
import edu.kit.kastel.ui.commands.ShowCommand;
import edu.kit.kastel.ui.commands.TagCommand;
import edu.kit.kastel.ui.commands.TaggedWithCommand;
import edu.kit.kastel.ui.commands.TodoCommand;
import edu.kit.kastel.ui.commands.ToggleCommand;
import edu.kit.kastel.ui.commands.UpcomingCommand;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class handles the user input and executes the commands.
 * This class was taken from the solution for Task number 5 of SS 2023.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public final class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = "\\s+";
    private static final String COMMAND_NOT_FOUND = "ERROR: Command '%s' not found%n";

    private final Procrastinot procrastinot;
    private final Map<String, Command> commands;
    private boolean running = false;


    /**
     * Intantiates a new command handler.
     * @param procrastinot the taskAdministrator object.
     */
    public CommandHandler(final Procrastinot procrastinot) {
        this.procrastinot = Objects.requireNonNull(procrastinot);
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Handles the user input.
     */
    public void handleUserInput() {
        this.running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNext()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the user input handling.
     */
    public void quit() {
        this.running = false;
    }


    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        if (!commands.containsKey(commandName)) {
            ResultType.FAILURE.printResult(COMMAND_NOT_FOUND, commandName);
            return;
        }

        commands.get(commandName).execute(commandArguments);
    }

    private void initCommands() {
        this.addCommand(new AddCommand(this, procrastinot));
        this.addCommand(new AddListCommand(this, procrastinot));
        this.addCommand(new AssignCommand(this, procrastinot));
        this.addCommand(new BeforeCommand(this, procrastinot));
        this.addCommand(new BetweenCommand(this, procrastinot));
        this.addCommand(new ChangeDateCommand(this, procrastinot));
        this.addCommand(new ChangePriorityCommand(this, procrastinot));
        this.addCommand(new DeleteCommand(this, procrastinot));
        this.addCommand(new DuplicatesCommand(this, procrastinot));
        this.addCommand(new FindCommand(this, procrastinot));
        this.addCommand(new ListCommand(this, procrastinot));
        this.addCommand(new QuitCommand(this, procrastinot));
        this.addCommand(new RestoreCommand(this, procrastinot));
        this.addCommand(new ShowCommand(this, procrastinot));
        this.addCommand(new TagCommand(this, procrastinot));
        this.addCommand(new TaggedWithCommand(this, procrastinot));
        this.addCommand(new TodoCommand(this, procrastinot));
        this.addCommand(new ToggleCommand(this, procrastinot));
        this.addCommand(new UpcomingCommand(this, procrastinot));
    }
    private void addCommand(final Command command) {
        this.commands.put(command.getCommandName(), command);
    }
}
