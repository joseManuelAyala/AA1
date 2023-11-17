package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Priority;
import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import edu.kit.kastel.ui.Result;
import java.time.LocalDate;

/**
 * This command adds a task to the Procrastinot System.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class AddCommand extends ProcrastinotCommand {
    private static final String  COMMAND_NAME = "add";
    private static final String TASK_NAME_REGEX = ".*\\s.*";
    private static final int NAME_INDEX = 0;
    private static final int UNEXPECTED_ARGUMENTS_LENGTH = 0;
    private static final int TASK_WITH_PRIORIOTY_LENGTH = 2;
    private static final int TASK_PRIORITY_INDEX = 1;
    private static final int TASK_WITH_DATE_LENGTH = 3;
    private static final int MAXIMAL_LENGHT = 3;
    private static final int TASK_DATE_INDEX = 2;
    private static final String ARGUMENTS_LENGTH_ERROR = "the arguments must contain at least the task name.";
    private static final String INVALID_TASK_ERROR = "the given task is not valid and can not be added";
    private static final String ADD_TASK_CONFIRMATION = "added %d: %s";
    private static final String MAXIMAL_ARGUMENTS_LENGTH_ERROR = "found more arguments than expected.";


    /**
     * Intantiates a new add command.
     * @param commandHandler the comandHandler
     * @param procrastinot the procrastinot instance.
     */
    public AddCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, UNEXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        Task taskToAdd = null;
        if (commandArguments.length > MAXIMAL_LENGHT) {
            return new Result(ResultType.FAILURE, MAXIMAL_ARGUMENTS_LENGTH_ERROR);
        }
        if (commandArguments.length == UNEXPECTED_ARGUMENTS_LENGTH || commandArguments[NAME_INDEX].isEmpty()
            || commandArguments[NAME_INDEX].matches(TASK_NAME_REGEX)) {
            return new Result(ResultType.FAILURE, ARGUMENTS_LENGTH_ERROR);
        }
        // Adds the task with only date or only priority.
        if (commandArguments.length == TASK_WITH_PRIORIOTY_LENGTH) {
            LocalDate date = getDate(commandArguments[TASK_PRIORITY_INDEX]);
            Priority priority = getPriority(commandArguments[TASK_PRIORITY_INDEX]);
            if (date != null || priority != null) {
                taskToAdd = new Task(commandArguments[NAME_INDEX], this.procrastinot.getTaskAddedNumber());
                taskToAdd.setDeadLine(date);
                //The setter in class Task prevents nullpointer exceptions.
                taskToAdd.setPriority(priority);
                procrastinot.addTask(taskToAdd);
                return new Result(ResultType.SUCCESS, (ADD_TASK_CONFIRMATION.formatted(
                    procrastinot.getTaskAddedNumber() - 1, taskToAdd.getName())));
            } else {
                return new Result(ResultType.FAILURE, INVALID_PRIORITY_ERROR);
            }
            // Adds the task with priority and date.
        } else if (commandArguments.length == TASK_WITH_DATE_LENGTH) {
            LocalDate date = getDate(commandArguments[TASK_DATE_INDEX]);
            Priority priority = getPriority(commandArguments[TASK_PRIORITY_INDEX]);
            if (date == null || priority == null) {
                //Since the arguments length is equal to 3 both the priority and date can not be null.
                return new Result(ResultType.FAILURE, INVALID_TASK_ERROR);
            }
            taskToAdd = new Task(commandArguments[NAME_INDEX], this.procrastinot.getTaskAddedNumber());
            taskToAdd.setDeadLine(date);
            taskToAdd.setPriority(priority);
            procrastinot.addTask(taskToAdd);
            return new Result(ResultType.SUCCESS, (ADD_TASK_CONFIRMATION.formatted(
                procrastinot.getTaskAddedNumber() - 1, taskToAdd.getName())));
        } else {
            taskToAdd = new Task(commandArguments[NAME_INDEX], this.procrastinot.getTaskAddedNumber());
            procrastinot.addTask(taskToAdd);
        }
        return new Result(ResultType.SUCCESS, (ADD_TASK_CONFIRMATION.formatted(
            procrastinot.getTaskAddedNumber() - 1, taskToAdd.getName())));
    }
}
