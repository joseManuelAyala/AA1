package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command shows a task and if posible the task subtasks in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class ShowCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "show";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int TASK_NUMBER_INDEX = 0;

    /**
     * Intantiates a new show command.
     * @param commandHandler the commandHandler
     * @param procrastinot the procrastinot instance.
     */
    public ShowCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        int taskId = getTaskId(commandArguments[TASK_NUMBER_INDEX]);
        Task task = procrastinot.getTask(taskId);
        if (task == null) {
            return new Result(ResultType.FAILURE, INVALID_TASK_NUMBER_ERROR);
        }
        printTask(task, START_INDENT_LEVEL);
        return null;
    }
}
