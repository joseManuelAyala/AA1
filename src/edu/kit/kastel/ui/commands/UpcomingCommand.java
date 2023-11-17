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
 * This command shows all of the task that their dealine is in the interval between the given date and the next 7 days
 *   in the Procrastinot System.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class UpcomingCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "upcoming";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final int DATE_INDEX = 0;
    private static final int DAYS_TO_ADD = 7;

    /**
     * Intantiates a upcoming with command.
     * @param commandHandler the coomandHandler
     * @param procrastinot the procrastinot instance.
     */
    public UpcomingCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        LocalDate currentDate = getDate(commandArguments[DATE_INDEX]);
        if (currentDate == null) {
            return new Result(ResultType.FAILURE, INVALID_DATE_ERROR);
        }
        LocalDate date = currentDate.plusDays(DAYS_TO_ADD);
        boolean isEmpty = false;
        boolean isEmptyResult = true;
        for (Task task : procrastinot.getTasks()) {
            isEmpty = printRecursively(task, START_INDENT_LEVEL, currentDate, date);
            if (isEmpty) {
                isEmptyResult = false;
            }
        }
        if (isEmptyResult) {
            return new Result(ResultType.SUCCESS, EMPTY_RESULT);
        }
        procrastinot.setUnprinted();
        return null;
    }

    private boolean printRecursively(Task toPrintTask, int indentLevel, LocalDate startDate, LocalDate endDate) {
        if (checkTaskDate(toPrintTask, startDate, endDate)) {
            List<Task> subTasks = toPrintTask.getSubTasks();
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + toPrintTask.toString());
            toPrintTask.setPrinted();
            for (Task task : subTasks) {
                printRecursively(task, indentLevel + 1, startDate, endDate);
            }
            return true;
        }
        return false;
    }

    private boolean checkTaskDate(Task task, LocalDate startDate, LocalDate endDate) {
        if (task.isDeleted() || task.isPrinted()
            || (task.getDeadline() == null && (task.getParentTask() != null && !task.getParentTask().isPrinted()))) {
            return false;
        }
        if (task.getParentTask() == null && isTaskDateValid(task, startDate, endDate)) {
            return true;
        }
        return task.getParentTask() != null && task.getParentTask().isPrinted()
            || task.getParentTask() != null && !isTaskDateValid(task.getParentTask(), startDate, endDate)
                && isTaskDateValid(task, startDate, endDate);
    }

    private boolean isTaskDateValid(Task task, LocalDate startDate, LocalDate endDate) {
        return task.getDeadline() == null || task.getDeadline().plusDays(1).isAfter(startDate)
            && task.getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(endDate);
    }
}
