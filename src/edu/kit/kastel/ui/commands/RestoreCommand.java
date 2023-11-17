package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.model.TaskList;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command restores a given task and if posible the Task Subtasks in the Procrastinot System.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class RestoreCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "restore";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    private static final int TASK_NUMBER_INDEX = 0;
    private static final String RESTORE_CONFIRMATION = "restored %s and %d subtasks";

    /**
     * Intantiates a restore command.
     * @param commandHandler the coomandHandler
     * @param procrastinot the procrastinot instance.
     */
    public RestoreCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        Task task = procrastinot.getDeletedTask(taskId);
        if (task == null) {
            return new Result(ResultType.FAILURE, TASK_NOT_FOUND_ERROR);
        }
        if (task.getParentTask() != null && task.getParentTask().isDeleted()) {
            if (!procrastinot.getParentList(task).isEmpty()) {
                //The task will be added once again to the corresponding TaskList and to the Procrastinot System.
                for (TaskList taskList : procrastinot.getParentList(task)) {
                    taskList.addTask(task);
                }
            }
            task.getParentTask().deleteSubTask(task);
            //If the parent task is deleted, the subtask wont be a subtask anymore. It will be considered a normal task.
            procrastinot.readdTask(task);
        }

        return new Result(ResultType.SUCCESS, RESTORE_CONFIRMATION.formatted(task.getName(), restoreTask(task)));
    }


    private int restoreTask(Task parentTask) {
        int sum = 0;
        //The given task will be added to all the system once again, so that it can be sorted.
        parentTask.setDeleted(false);
        procrastinot.addjustList(parentTask);
        for (Task task : parentTask.getSubTasks()) {
            if (task.isDeleted()) {
                sum += restoreTask(task) + 1;
            }
        }
        return sum;
    }
}
