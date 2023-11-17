package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.TaskList;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command adds a TaskList to the Procrastinot System.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class AddListCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "add-list";
    private static final String LIST_NAME_REGEX = "^[A-Za-z]+$";
    private static final String INVALID_NAME_ERROR = "the list name does not have the correct format";
    private static final String LIST_EXIST_ERROR = "the list already exist and can not be added.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int LIST_NAME_INDEX = 0;
    private static final String ADDED_LIST_FORMAT = "added %s";

    /**
     * Intantiates a add list command.
     * @param commandHandler the comandHandler
     * @param procrastinot the Procrastinot instance.
     */
    public AddListCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        if (commandArguments.length != EXPECTED_ARGUMENTS_LENGTH) {
            return new Result(ResultType.FAILURE, MORE_ARGUMENTS_THAN_EXPECTED.formatted(COMMAND_NAME,
                EXPECTED_ARGUMENTS_LENGTH));
        }
        //The list name must contain the name regex conditions.
        if (!commandArguments[LIST_NAME_INDEX].matches(LIST_NAME_REGEX)) {
            return new Result(ResultType.FAILURE, INVALID_NAME_ERROR);
        }
        //The new TaskList name can not already be in the Procrastinot System.
        if (procrastinot.containsTaskList(commandArguments[LIST_NAME_INDEX])) {
            return new Result(ResultType.FAILURE, LIST_EXIST_ERROR);
        }
        TaskList taskListToAdd = new TaskList(commandArguments[LIST_NAME_INDEX]);
        procrastinot.addList(taskListToAdd);
        return new Result(ResultType.SUCCESS, ADDED_LIST_FORMAT.formatted(taskListToAdd.getName()));
    }
}
