package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * This command shows all the duplicates Tasks in the Procrastinot system.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */
public class DuplicatesCommand extends ProcrastinotCommand {
    private static final String COMMAND_NAME = "duplicates";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;
    private static final String DUPLICATES_LIST_FORMAT = "Found %d duplicates: %s";
    private static final String DUPLICATES_DELIMITER = ", ";

    /**
     * Intantiates a duplicates command.
     * @param commandHandler the commandHandler
     * @param procrastinot the Procrastinot instance.
     */
    public DuplicatesCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
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
        List<Integer> duplicatesId = procrastinot.getDuplicates();
        //Sorts the duplicates tasks id's.
        Collections.sort(duplicatesId);
        StringJoiner stringJoiner = new StringJoiner(DUPLICATES_DELIMITER);
        for (int taskId : duplicatesId) {
            int number = taskId + 1;
            stringJoiner.add(Integer.toString(number));
        }
        //The size of the list determines the number of duplicaste Tasks.
        return new Result(ResultType.SUCCESS, DUPLICATES_LIST_FORMAT.formatted(duplicatesId.size(),
            stringJoiner.toString()));
    }
}
