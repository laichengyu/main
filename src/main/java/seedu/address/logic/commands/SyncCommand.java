package seedu.address.logic.commands;

/**
 * Updates all coins in the coin book with latest data
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_SUCCESS = "Updated all coins with latest data";

    @Override
    public CommandResult execute() {
        model.updateAll();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
