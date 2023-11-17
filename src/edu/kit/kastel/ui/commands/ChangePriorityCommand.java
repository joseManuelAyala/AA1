package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Priority;
import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command changes the Task Priority of one of the Procrastinot System task.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class ChangePriorityCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "change-priority";
    private static final int NO_PRIORITY_ARGUMENTS_LENGTH = 0;
    private static final int TASK_NUMBER_INDEX = 0;
    private static final int PRIORITY_INDEX = 1;
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;
    private static final int TASK_PRIORITY_ARGUEMENTS_LENGTH = 2;


    /**
     * Intantiates a change priority command.
     * @param commandHandler the comandHandler
     * @param procrastinot the procrastinot instance.
     */
    public ChangePriorityCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        if (commandArguments.length > TASK_PRIORITY_ARGUEMENTS_LENGTH || commandArguments.length
            == NO_PRIORITY_ARGUMENTS_LENGTH) {
            return new Result(ResultType.FAILURE, MORE_ARGUMENTS_THAN_EXPECTED.formatted(COMMAND_NAME,
                EXPECTED_ARGUMENTS_LENGTH));
        }
        int taskId = getTaskId(commandArguments[TASK_NUMBER_INDEX]);
        Priority priority;
        if (commandArguments.length == PRIORITY_INDEX || commandArguments[PRIORITY_INDEX].isEmpty()) {
            //If no priority argument is found, the priority will be seted to the default priority.
            priority = Priority.ND;
        } else {
            priority = getPriority(commandArguments[PRIORITY_INDEX]);
        }
        //if the priority argument is found but does not equal any of the System priority, an error will be thrown.
        if (priority == null) {
            return new Result(ResultType.FAILURE, INVALID_PRIORITY_ERROR);
        }
        Task task = procrastinot.getTask(taskId);
        if (task == null) {
            return new Result(ResultType.FAILURE, TASK_NOT_FOUND_ERROR);
        }
        task.setPriority(priority);
        return new Result(ResultType.SUCCESS, CHANGE_CONFIRMATION.formatted(task.getName(),
            priority.getValue()));
    }
}
