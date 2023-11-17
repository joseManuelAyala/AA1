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
 * This command shows all the Procrastinot Tasks that their dead line is between to given dates.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class BetweenCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "between";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;
    private static final int START_DATE_INDEX = 0;
    private static final int END_DATE_INDEX = 1;
    private static final String INVALID_DATES_ERROR = "the given date is not a valid date.";

    /**
     * Intantiates a between command.
     * @param commandHandler the ComandHandler.
     * @param procrastinot the Procrastinot instance.
     */
    public BetweenCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        LocalDate startDate = getDate(commandArguments[START_DATE_INDEX]);
        LocalDate endDate = getDate(commandArguments[END_DATE_INDEX]);
        //Both dates must be valid.
        if (startDate == null || endDate == null) {
            return new Result(ResultType.FAILURE, INVALID_DATES_ERROR);
        }
        boolean didPrint = false;
        boolean isEmptyResult = true;
        for (Task task : procrastinot.getTodoTask()) {
            didPrint = printRecursively(task, START_INDENT_LEVEL, startDate, endDate);
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


    private boolean printRecursively(Task taskToPrint, int indentLevel, LocalDate startDate, LocalDate endDate) {
        if (checkTaskDateConditions(taskToPrint, startDate, endDate)
            && taskFormatCondition(taskToPrint, startDate, endDate)) {
            List<Task> subTasks = taskToPrint.getSubTasks();
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + taskToPrint.toString());
            taskToPrint.setPrinted();
            for (Task task : subTasks) {
                printRecursively(task, indentLevel + 1, startDate, endDate);
            }
            return true;
        }
        return false;
    }

    private boolean taskFormatCondition(Task task, LocalDate startDate, LocalDate endDate) {
        if (task.getDeadline() == null && (task.getParentTask() != null && !task.getParentTask().isPrinted())) {
            return false;
        }
        return task.getParentTask() == null || (task.getParentTask().isPrinted()
            || (task.getParentTask().getDeadline() != null
            && task.getParentTask().getDeadline().minusDays(DAYS_BEFORE_DATE).isAfter(startDate))
            && task.getParentTask().getDeadline().plusDays(DAYS_BEFORE_DATE).isBefore(endDate));
    }

    private boolean checkParentTaskDate(Task task, LocalDate startDate, LocalDate endDate) {
        return task != null
            && (isBetweeDates(task, startDate, endDate)
            || checkParentTaskDate(task.getParentTask(), startDate, endDate));
    }
    private boolean checkTaskDateConditions(Task task, LocalDate startDate, LocalDate endDate) {
        boolean taskStatus = !task.isDeleted() && !task.isPrinted();
        boolean taskDates = task.getDeadline() == null || isBetweeDates(task, startDate, endDate);
        boolean taskParentDate = task.getParentTask() != null && task.getParentTask().getDeadline() == null;
        return taskStatus && (taskDates || taskParentDate || checkParentTaskDate(task, startDate, endDate));
    }
}
