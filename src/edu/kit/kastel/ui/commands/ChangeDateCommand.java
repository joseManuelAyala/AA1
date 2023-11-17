package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.time.LocalDate;

/**
 * This command changes the dead line of one of the Procrastinot System Task.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */

public class ChangeDateCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "change-date";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;
    private static final int TASK_NUMBER_INDEX = 0;
    private static final int DATE_INDEX = 1;

    /**
     * Intantiates a change date command.
     * @param commandHandler the CommandHandler
     * @param procrastinot the Procrastinot instance.
     */
    public ChangeDateCommand(CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        LocalDate newDate = getDate(commandArguments[DATE_INDEX]);
        Task taskToChange = procrastinot.getTask(taskId);
        if (taskToChange == null) {
            return new Result(ResultType.FAILURE, INVALID_TASK_NUMBER_ERROR);
        } else if (newDate == null) {
            return new Result(ResultType.FAILURE, INVALID_DATE_ERROR);
        }
        //The Task Deadline is seted to the given date.
        taskToChange.setDeadLine(newDate);
        return new Result(ResultType.SUCCESS, CHANGE_CONFIRMATION.formatted(taskToChange.getName(),
            newDate.toString()));
    }
}
