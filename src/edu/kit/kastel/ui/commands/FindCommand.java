package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.util.List;

/**
 * This command shows all the task of the Procrastinot System, only if the task name contains a given string part.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class FindCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "find";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int STRING_INDEX = 0;

    /**
     * Intantiates a find command.
     * @param commandHandler the commandHandler
     * @param procrastinot the Procrastinot instance.
     */
    public FindCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        String nameToFind = commandArguments[STRING_INDEX];

        List<Task> procrastinotTasks = procrastinot.getTasks();
        boolean didPrint = false;
        boolean isEmptyResult = true;
        for (Task task: procrastinotTasks) {
            didPrint = prinRecursively(task, START_INDENT_LEVEL, nameToFind);
            if (didPrint) {
                isEmptyResult = false;
            }
        }
        if (isEmptyResult) {
            return new Result(ResultType.SUCCESS, EMPTY_RESULT);
        }
        procrastinot.setUnprinted();
        return null;
    }

    private boolean prinRecursively(final Task taskToPrint, final int indentLevel, final String nameToFind) {
        if (!taskToPrint.isDeleted() && !taskToPrint.isPrinted() && (taskToPrint.containsString(nameToFind)
            || checkParentTask(taskToPrint, nameToFind))) {
            List<Task> subTasks = taskToPrint.getSubTasks();
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + taskToPrint.toString());
            taskToPrint.setPrinted();
            for (Task task : subTasks) {
                prinRecursively(task, indentLevel + 1, nameToFind);
            }
            return true;
        }
        return false;
    }

    private boolean checkParentTask(final Task task, final String name) {
        return task != null && (task.containsString(name) || checkParentTask(task.getParentTask(), name));
    }
}
