package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;

import java.util.List;

/**
 * This command shows all of the to-do tasks in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class TodoCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "todo";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;

    /**
     * Intantiates a to-do command.
     *
     * @param commandHandler    the coomandHandler
     * @param procrastinot the procrastinot instance.
     */
    public TodoCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        List<Task> todoTasks = procrastinot.getTodoTask();
        boolean didPrint = false;
        boolean isEmptyResult = true;

        for (Task task : todoTasks) {
            didPrint = printRecursively(task, START_INDENT_LEVEL);
            if (didPrint) {
                isEmptyResult = false;
            }
        }
        if (isEmptyResult) {
            return new Result(ResultType.SUCCESS, EMPTY_RESULT);
        }
        //Sets all the Procrastinot Task to unprinted.
        procrastinot.setUnprinted();
        return null;
    }


    private boolean printRecursively(final Task toPrintTask, final int indentLevel) {
        if (!toPrintTask.isDeleted() && !toPrintTask.isPrinted() && printTaskConditions(toPrintTask)
            && (containsTodoTask(toPrintTask) || !toPrintTask.isDone())) {
            List<Task> subTasks = toPrintTask.getSubTasks();
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + toPrintTask.toString());
            toPrintTask.setPrinted();
            for (Task task : subTasks) {
                printRecursively(task, indentLevel + 1);
            }
            return true;
        }
        return false;
    }

    private boolean containsTodoTask(Task todoTask) {
        for (Task task : todoTask.getSubTasks()) {
            if (containsTodoTask(task) || !task.isDone()) {
                return true;
            }
        }
        return false;
    }
    private boolean printTaskConditions(Task task) {
        return task.getParentTask() == null || task.getParentTask().isPrinted();
    }
}
