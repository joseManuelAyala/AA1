package edu.kit.kastel.util;

import edu.kit.kastel.model.Procrastinot;
import edu.kit.kastel.ui.CommandHandler;

/**
 * Main entry point for starting the Procrastinot system.
 * Starts the interaction with the user.
 * @author ucxug
 * @version 1.0
 */
public final class Main {
    private static final String CLASS_NOT_INSTANTIATABLE = "Utility class cannot be initialized.";
    private static final String NO_PARAMETERS_ALLOWED_ERROR =
        "No arguments are allowed here.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;

    private Main() {
        throw new IllegalStateException(CLASS_NOT_INSTANTIATABLE);

    }

    /**
     * The main method is the entry point for the programm, it serves to handle all the user interaction with
     * the programm.
     * @param args the command arguments, but for this case there are not used.
     * @throws IllegalArgumentException if the arguments lenght if not equal to 0.
     *
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if (args.length != EXPECTED_ARGUMENTS_LENGTH) {
            throw new IllegalArgumentException(NO_PARAMETERS_ALLOWED_ERROR);
        } else {
            Procrastinot procrastinot = new Procrastinot();
            CommandHandler commandHandler = new CommandHandler(procrastinot);
            commandHandler.handleUserInput();
        }
    }


}
