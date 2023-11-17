package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command quits Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class QuitCommand extends ProcrastinotCommand {

    private static final String COMMAND_NAME = "quit";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;

    /**
     * Intantiates a quit command.
     * @param commandHandler the commandHandler
     * @param procrastinot the procrastinot instance.
     */
    public QuitCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }
    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        commandHandler.quit();
        return null;
    }
}
