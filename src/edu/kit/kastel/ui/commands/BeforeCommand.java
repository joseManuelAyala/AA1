package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.time.LocalDate;
import java.util.List;

/**
 * This command show all the Procrastinot Tasks that their dead line is before a given date.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class BeforeCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "before";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int DATE_INDEX = 0;

    /**
     * Intantiates a before command.
     * @param commandHandler    the comandHandler
     * @param procrastinot      the Procrastinot instance.
     */
    public BeforeCommand(CommandHandler commandHandler, final Procrastinot procrastinot) {
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

        LocalDate currentDate = getDate(commandArguments[DATE_INDEX]);
        if (currentDate == null) {
            return new Result(ResultType.FAILURE, INVALID_DATE_ERROR);
        }
        //If no Tasks is found the result will be an empty String.
        boolean didPrint = false;
        boolean isEmptyResult = true;
        for (Task task : procrastinot.getTodoTask()) {
            didPrint = printRecursively(task, START_INDENT_LEVEL, currentDate);
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

    private boolean printRecursively(Task parentTask, int indentLevel, LocalDate date) {
        if (checkTaskDateConditions(parentTask, date)) {
            List<Task> subTasks = parentTask.getSubTasks();
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + parentTask.toString());
            parentTask.setPrinted();
            for (Task task : subTasks) {
                printRecursively(task, indentLevel + 1, date);
            }
            return true;
        }
        return false;
    }
    private boolean checkTaskDateConditions(Task task, LocalDate taskDate) {
        if (task.isDeleted()  || task.isPrinted()) {
            return false;
        }
        if (task.getDeadline() == null && (task.getParentTask() != null && !task.getParentTask().isPrinted())) {
            return false;
        }
        if (!taskParentDateCondition(task, taskDate)) {
            return false;
        }
        if (task.getParentTask() != null && (task.getDeadline() != null
            && (task.getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(taskDate)))) {
            return true;
        }
        return isDateBetweenDates(task, taskDate);
    }


    private boolean taskParentDateCondition(Task task, LocalDate taskDate) {
        return (task.getParentTask() == null || (task.getDeadline() != null || task.getParentTask().isPrinted()))
            && (task.getParentTask() != null
                || (task.getDeadline() != null && task.getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(taskDate)));
    }


    private boolean isDateBetweenDates(Task task, LocalDate taskDate) {
        //The task date must be before the given date.
        boolean validTaskDate = task.getDeadline() == null
            || task.getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(taskDate);
        boolean taskParentValidDate = task.getParentTask() != null;

        //If the parent task is printed, or the parent task date is before the given date the subtask will be printed.
        boolean isParentTaskPrinted = (task.getParentTask() == null || task.getParentTask().isPrinted())
            || (task.getParentTask().getDeadline() != null
            && !task.getParentTask().getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(taskDate));
        return (validTaskDate || taskParentValidDate) && isParentTaskPrinted;
    }
}
