package edu.kit.kastel.ui.commands;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.model.Task;
import edu.kit.kastel.ui.CommandHandler;
import edu.kit.kastel.ui.Result;
import edu.kit.kastel.ui.ResultType;
import edu.kit.kastel.ui.ProcrastinotCommand;
import java.util.List;

/**
 * This command shows all of the tagged with a given tag, tasks in the Procrastinot System.
 * @author Programmieren-Team
 * @author ucxug
 * @version 1.0
 */

public class TaggedWithCommand extends ProcrastinotCommand {

    private static final String COMMAND_NAME = "tagged-with";
    private static final String TAG_NOT_FOUND = "there is are no Tasks marked with the given tag";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final String TAG_REGEX = "^[A-Za-z0-9]+$";
    private static final int TAG_INDEX = 0;

    /**
     * Intantiates a tagged with command.
     * @param commandHandler the commandHandler
     * @param procrastinot the procrastinot instance.
     */
    public TaggedWithCommand(final CommandHandler commandHandler, final Procrastinot procrastinot) {
        super(COMMAND_NAME, commandHandler, procrastinot, EXPECTED_ARGUMENTS_LENGTH);
    }

    /**
     * Executes the command.
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        if (commandArguments.length > EXPECTED_ARGUMENTS_LENGTH) {
            return new Result(ResultType.FAILURE, MORE_ARGUMENTS_THAN_EXPECTED.formatted(COMMAND_NAME,
                EXPECTED_ARGUMENTS_LENGTH));
        }
        String tag = commandArguments[TAG_INDEX];
        if (!tag.matches(TAG_REGEX)) {
            //The tag must match the given regex to be considered as valid.
            return new Result(ResultType.FAILURE, TAG_NOT_FOUND);
        }
        //Gets all the taggged task with the given tag.
        List<Task> taggedTasks = procrastinot.getTaggedTask(tag);
        if (taggedTasks.isEmpty()) {
            return new Result(ResultType.SUCCESS, EMPTY_RESULT);
        } else {
            for (Task task : taggedTasks) {
                printTask(task, START_INDENT_LEVEL);
            }
        }
        return null;
    }
}
