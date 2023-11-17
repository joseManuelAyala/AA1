package edu.kit.kastel.ui;

import edu.kit.kastel.model.Priority;
import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * This class represents a command for the Procrastinot object.
 * This class was taken from the solution for Task number 5 of SS 2023 but has new implementations and methods.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */

public abstract class ProcrastinotCommand extends Command {
    /**
     * Error for when a task has not been found.
     */
    protected static final String TASK_NOT_FOUND_ERROR = " the task can not be found";
    /**
     * The start indent level for printing the tasks.
     */
    protected static final int START_INDENT_LEVEL = 0;
    /**
     * Error for when a List has not be found.
     */
    protected static final String LIST_NOT_FOUND_ERROR = "the given list can not be found";
    /**
     * Error for when the given date is not valid.
     */
    protected static final String INVALID_DATE_ERROR = "the given date is not valid.";
    /**
     * Confirmation message for when the date or priority of a task is changed.
     */
    protected static final String CHANGE_CONFIRMATION = "changed %s to %s";
    /**
     * Delimiter, Indent for printing a task or a subtask.
     */
    protected static final String TASK_LEVEL_REPRESENTATION = "  ";
    /**
     * Empty string for when a given command result is empty and needs to be printed.
     */
    protected static final String EMPTY_RESULT = "";
    /**
     * Error for when the given priority is not valid.
     */
    protected static final String INVALID_PRIORITY_ERROR = "the given Priority is not valid.";
    /**
     * Error for when the given task Id is not valid.
     */
    protected static final String INVALID_TASK_NUMBER_ERROR = "the given task number is not valid or not found";
    /**
     * Error for when the given command does not expect any argument.
     */
    protected static final String NO_ARGUMENTS_EXPECETED_ERROR = "the command %s does not expect any arguments.";
    /**
     * Error for when the given command becomes more arguments than expected.
     */
    protected static final String MORE_ARGUMENTS_THAN_EXPECTED = "the command %s needs only %d arguments";
    /**
     * Represents the days to add to a given date to check if it is before/after a given date.
     */
    protected static final int DAYS_BEFORE_DATE = 1;
    private static final String NOT_EXPECTED_ARGS_LENGTH_ERROR = "Expected %d arguments but got %d";
    private static final int DAY_INDEX = 2;

    private static final String DATE_SEPARATOR = "-";
    private static final String DATE_FORMAT = "";
    /**
     * The Procrastinot instance.
     */
    protected final Procrastinot procrastinot;
    private final int expectedNumberOfArguments;



    /**
     * Constructs a new command.
     * @param commandName               the name of the command.
     * @param commandHandler            the command handler.
     * @param procrastinot              the procrastinot instance.
     * @param expectedNumberOfArguments the expected leght for the command arguments.
     */
    protected ProcrastinotCommand(String commandName, CommandHandler commandHandler,
                                  final Procrastinot procrastinot, int expectedNumberOfArguments) {
        super(commandName, commandHandler);
        this.procrastinot = Objects.requireNonNull(procrastinot);
        this.expectedNumberOfArguments = expectedNumberOfArguments;
    }

    @Override
    public final void execute(String[] commandArguments) {
        if (commandArguments.length < expectedNumberOfArguments) {
            ResultType.FAILURE.printResult(NOT_EXPECTED_ARGS_LENGTH_ERROR, expectedNumberOfArguments,
                commandArguments.length);
            return;
        }

        Result result = executeTaskCommand(commandArguments);
        if (result != null) {
            result.getType().printResult(result.getMessage());
        }
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    protected abstract Result executeTaskCommand(String[] commandArguments);

    /**
     * Gets the priority mathcing the given string.
     * @param priorityValue the string to be checked.
     * @return the Priority if the string matches a priority, null otherwise.
     */
    protected Priority getPriority(String priorityValue) {

        Priority newPriority = null;
        for (Priority priority: Priority.values()) {
            if (priority.getValue().equals(priorityValue)) {
                newPriority = priority;
                return newPriority;
            }
        }
        return null;
    }

    /**
     * Constructs a new LocalDate object if the given representation is a valid date.
     * @param dateRepresentation the string to be checked.
     * @return the Local Date if the string matches a valid date, null otherwise.
     */
    protected LocalDate getDate(final String dateRepresentation) {
        LocalDate dateTime;
        int day;
        String dateRepresentationFormat = dateRepresentation.replace(DATE_SEPARATOR, DATE_FORMAT);
        try {
            dateTime = LocalDate.parse(dateRepresentationFormat, DateTimeFormatter.BASIC_ISO_DATE);

        } catch (DateTimeParseException e) {
            return null;
        }
        try {
            String[] dateParts = dateRepresentation.split(DATE_SEPARATOR);
            day = Integer.parseInt(dateParts[DAY_INDEX]);

        } catch (NumberFormatException e) {
            return null;
        }
        if (day > dateTime.getMonth().length(dateTime.isLeapYear())) {
            return null;
        }
        return dateTime;
    }

    /**
     * Prints the task in a recursive form.It also prints all the subtasks from the given task.
     * @param taskToPrint the task to be printed.
     * @param indentLevel the identitation level for the task to be printed.
     */
    protected void printTask(final Task taskToPrint, final int indentLevel) {
        if (!taskToPrint.isDeleted()) {
            String indent = TASK_LEVEL_REPRESENTATION.repeat(indentLevel);
            System.out.println(indent + taskToPrint.toString());
            for (Task subtask : taskToPrint.getSubTasks()) {
                printTask(subtask, indentLevel + 1);
            }
        }
    }

    /**
     * Returns the task number for a given task
     * @param numberRepresentation the String containing the task number.
     * @return the task number if the string has the correct format, null otherwise.
     */
    protected int getTaskId(final String numberRepresentation) {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberRepresentation);
        } catch (NumberFormatException e) {
            return -1;
        }
        return taskNumber;
    }

    /**
     * Checks if the deadline of the given task falls within the specified date range
     * @param taskToCheck the Task to be checked.
     * @param startDate the start date of the range.
     * @param endDate the end date of the range.
     * @return true if the tasks deadline is within the date range or has no deadline,
     *          false otherwise.
     */
    protected boolean isBetweeDates(Task taskToCheck, final LocalDate startDate, final LocalDate endDate) {
        if (taskToCheck.getDeadline() == null) {
            return true;
        } else {
            for (Task task : taskToCheck.getSubTasks()) {
                return isBetweeDates(task, startDate, endDate);
            }
        }
        return taskToCheck.getDeadline().plusDays(DAYS_BEFORE_DATE).isAfter(startDate)
            && taskToCheck.getDeadline().minusDays(DAYS_BEFORE_DATE).isBefore(endDate);
    }
}
