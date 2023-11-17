package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command toggles a given task and if posible the task subtask in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class ToggleCommand extends ProcrastinotCommand {

    private static final String COMMAND_NAME = "toggle";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int TASK_NUMBER_INDEX = 0;
    private static final String TOGGLE_CONFIRMATION_FORMAT = "toggled %s and %d subtasks";

    /**
     * Intantiates a toggle with command.
     * @param commandHandler the commandHandler
     * @param procrastinot the procrastinot instance.
     */
    public ToggleCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
            //The task must be first found in the Procrastinot System.
            return new Result(ResultType.FAILURE, TASK_NOT_FOUND_ERROR);
        }
        boolean taskDoneStatus = !task.isDone();
        task.setDone(taskDoneStatus);
        //Sets all the subtasks to the parent task done value.
        for (Task tasks : task.getSubTasks()) {
            if (!tasks.isDeleted()) {
                setDoneStatus(task, tasks);
            }
        }
        return new Result(ResultType.SUCCESS, TOGGLE_CONFIRMATION_FORMAT.formatted(task.getName(), toggleTaks(task)));
    }

    private void setDoneStatus(Task parentTask, Task subtask) {
        subtask.setDone(parentTask.isDone());
        for (Task task : subtask.getSubTasks()) {
            setDoneStatus(parentTask, task);
        }
    }

    private int toggleTaks(Task parentTask) {
        int sum = 0;
        for (Task task : parentTask.getSubTasks()) {
            if (!task.isDeleted()) {
                sum += toggleTaks(task) + 1;
            }
        }
        return sum;
    }
}
