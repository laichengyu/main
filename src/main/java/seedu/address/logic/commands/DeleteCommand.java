package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.exceptions.CoinNotFoundException;

/**
 * Deletes a coin identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the coin identified by the index number used in the last coin listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_COIN_SUCCESS = "Deleted Coin: %1$s";

    private final Index targetIndex;

    private Coin coinToDelete;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(coinToDelete);
        try {
            model.deleteCoin(coinToDelete);
        } catch (CoinNotFoundException pnfe) {
            throw new AssertionError("The target coin cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_COIN_SUCCESS, coinToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Coin> lastShownList = model.getFilteredCoinList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COIN_DISPLAYED_INDEX);
        }

        coinToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCommand) other).targetIndex) // state check
                && Objects.equals(this.coinToDelete, ((DeleteCommand) other).coinToDelete));
    }
}
