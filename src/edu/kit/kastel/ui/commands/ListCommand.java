package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.model.TaskList;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.util.List;

/**
 * This command shows all the tasks of a TaskList in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class ListCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "list";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int LIST_NAME_INDEX = 0;
    private static final String LIST_NOT_FOUND_ERROR = "the list can not be found";


    /**
     * Intantiates a list command.
     * @param commandHandler the commandHandler
     * @param procrastinot the procrastinot instance.
     */
    public ListCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        String taskListName = commandArguments[LIST_NAME_INDEX];
        TaskList taskList = procrastinot.getTaskList(taskListName);
        //The TaskList must be already added in to the Procrastinot system.
        if (taskList == null) {
            return new Result(ResultType.FAILURE, LIST_NOT_FOUND_ERROR);
        }
        List<Task> taskListTasks = taskList.getTasks();
        if (taskListTasks.isEmpty()) {
            return new Result(ResultType.SUCCESS, EMPTY_RESULT);
        } else  {
            for (Task task : taskListTasks) {
                printTask(task, START_INDENT_LEVEL);
            }
        }
        return null;
    }
}
