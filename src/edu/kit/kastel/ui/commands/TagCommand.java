package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.model.TaskList;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

/**
 * This command tags a task with a given tag in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class TagCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "tag";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;
    private static final int TASK_NUMBER_INDEX = 0;
    private static final int TAG_INDEX = 1;
    private static final int LIST_INDEX_NAME = 0;
    private static final String  TAG_EXIST_ERROR = "the task already contains the given tag";
    private static final String TAG_REGEX = "^[A-Za-z0-9]+$";
    private static final String TAG_REGEX_ERROR = "the tag does not have the correct format";
    private static final String INVALID_LIST_ERROR = "the list can not be tagged with the given tag";
    private static final String TAGGED_CONFIRMATION = "tagged %s with %s";

    /**
     * Intantiates a tag command.
     * @param commandHandler the coomandHandler
     * @param procrastinot the procrastinot instance.
     */
    public TagCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        String tag = commandArguments[TAG_INDEX];
        if (!tag.matches(TAG_REGEX)) {
            //The tag must match the given regex to be considered as valid.
            return new Result(ResultType.FAILURE, TAG_REGEX_ERROR);
        }
        if (taskId != -1) {
            //if the Task is not found it will be considered to tag a TaskList.
            Task toTagTask = procrastinot.getTask(taskId);
            if (toTagTask == null) {
                return new Result(ResultType.FAILURE, INVALID_TASK_NUMBER_ERROR);
            }
            if (toTagTask.getTags().contains(tag)) {
                return new Result(ResultType.FAILURE, TAG_EXIST_ERROR);
            }
            toTagTask.addTag(tag, false); //Tags the Task. The tag does not come from a TaskList
            return new Result(ResultType.SUCCESS, TAGGED_CONFIRMATION.formatted(toTagTask.getName(), tag));
        }
        return tagList(commandArguments[LIST_INDEX_NAME], tag);
    }


    private Result tagList(final String taskListName, final String tag) {
        TaskList toTagTaskList = procrastinot.getTaskList(taskListName);
        if (toTagTaskList == null || toTagTaskList.containsTag(tag)) {
            //if the TaskList is not found or the TaskList already contains the given tag it will cause an error.
            return new Result(ResultType.FAILURE, INVALID_LIST_ERROR);
        }
        toTagTaskList.addTag(tag);
        return new Result(ResultType.SUCCESS, TAGGED_CONFIRMATION.formatted(toTagTaskList.getName(), tag));

    }
}
