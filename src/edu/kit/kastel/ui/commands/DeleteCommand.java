package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command deletes one of the Procrastinot System Task.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class DeleteCommand extends ProcrastinotCommand {

    private static final String COMMAND_NAME = "delete";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int TASK_NUMBER_INDEX = 0;
    private static final String DELETE_CONFIRMATION_FORMAT = "deleted %s and %d subtasks";


    /**
     * Intantiates a delete command.
     * @param commandHandler the coomandHandler
     * @param procrastinot the procrastinot instance.
     */
    public DeleteCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        if (commandArguments.length > EXPECTED_ARGUMENTS_LENGTH) {
            return new Result(ResultType.FAILURE, NO_ARGUMENTS_EXPECETED_ERROR.formatted(COMMAND_NAME));
        }
        int taskId = getTaskId(commandArguments[TASK_NUMBER_INDEX]);
        Task task = procrastinot.getTask(taskId);
        if (task == null) {
            return new Result(ResultType.FAILURE, TASK_NOT_FOUND_ERROR);
        }
        //The task and subtasks will be recursively deleted.
        return new Result(ResultType.SUCCESS, DELETE_CONFIRMATION_FORMAT.formatted(task.getName(), task.deleteTask()));
    }
}
