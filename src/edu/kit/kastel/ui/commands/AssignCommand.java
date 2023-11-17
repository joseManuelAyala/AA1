package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.TaskList;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command assigns a task to another task ,making it a subtask.
 * The task can also be assigned to a TaskList.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class AssignCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "assign";
    private static final String ASSIGN_CONFIRMATION_FORMAT = "assigned %s to %s";
    private static final String TASK_EXIST_IN_LIST_ERROR = "the given task is already in the list";
    private static final String TASK_PARENT_ERROR = "the subtask can not be added the the main task.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;
    private static final int TASK_NUMBER_INDEX = 1;
    private static final int LIST_NAME_INDEX = 1;
    private static final int INEXISTENT_TASK_NUMBER = -1;
    private static final int SUB_TASK_NUMBER_INDEX = 0;

    /**
     * Intantiates a assign command.
     * @param commandHandler the comandHandler
     * @param procrastinot the procrastinot instance.
     */
    public AssignCommand(CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(String[] commandArguments) {
        if (commandArguments.length != EXPECTED_ARGUMENTS_LENGTH) {
            return new Result(ResultType.FAILURE, MORE_ARGUMENTS_THAN_EXPECTED.formatted(COMMAND_NAME,
                EXPECTED_ARGUMENTS_LENGTH));
        }
        boolean isList = false;
        int subTaskId = getTaskId(commandArguments[SUB_TASK_NUMBER_INDEX]);
        Task subTask = procrastinot.getTask(subTaskId);
        if (subTaskId == INEXISTENT_TASK_NUMBER || subTask == null) {
            //The subtask to be asssigned must already be in the procrastinot system.
            return new Result(ResultType.FAILURE, INVALID_TASK_NUMBER_ERROR);
        }

        int parentTaskId = getTaskId(commandArguments[TASK_NUMBER_INDEX]);
        if (parentTaskId == INEXISTENT_TASK_NUMBER) {
            //if the parent task is not found the subtask will be added to a TaskList.
            isList = true;
        }
        if (!isList) {
            //The subtask will be added to the parent task, if the parent task is in the Procrastinot System.
            Task task = procrastinot.getTask(parentTaskId);
            if (task == null) {
                return new Result(ResultType.FAILURE, TASK_NOT_FOUND_ERROR);
            } else if (subTask.getSubTasks().contains(task) || task.getSubTasks().contains(subTask)
                || subTask.equals(task)) {
                return new Result(ResultType.FAILURE, TASK_PARENT_ERROR);
            } else {
                procrastinot.addSubTask(task, subTask);
                return new Result(ResultType.SUCCESS, ASSIGN_CONFIRMATION_FORMAT.formatted(subTask.getName(),
                    task.getName()));
            }
        }
        return assignToTaskList(commandArguments[LIST_NAME_INDEX], subTask);
    }


    private Result assignToTaskList(String taskListName, Task subTask) {
        //The subtask will be added to the given Task list, if it is found.
        TaskList taskList = procrastinot.getTaskList(taskListName);
        if (taskList == null) {
            return new Result(ResultType.FAILURE, LIST_NOT_FOUND_ERROR);
        } else if (taskList.containsTask(subTask)) {
            //If the TaskList already contains the given subtask it will cause an error.
            return new Result(ResultType.FAILURE, TASK_EXIST_IN_LIST_ERROR);
        }
        taskList.addTask(subTask);
        return new Result(ResultType.SUCCESS, ASSIGN_CONFIRMATION_FORMAT.formatted(subTask.getName(),
            taskList.getName()));
    }
}
