//@@author laichengyu

package seedu.address.logic;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BuyCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NotifyCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SellCommand;
import seedu.address.logic.commands.SyncCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.ViewCommand;

/**
 * Stores a list of all available commands
 */
public class CommandList {
    private List<String> commandList;

    public CommandList() {
        commandList = Arrays.asList(HelpCommand.COMMAND_WORD, AddCommand.COMMAND_WORD, BuyCommand.COMMAND_WORD,
                SellCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD, ClearCommand.COMMAND_WORD,
                TagCommand.COMMAND_WORD, ListCommand.COMMAND_WORD, FindCommand.COMMAND_WORD, ViewCommand.COMMAND_WORD,
                NotifyCommand.COMMAND_WORD, SortCommand.COMMAND_WORD, HistoryCommand.COMMAND_WORD, UndoCommand.COMMAND_WORD,
                RedoCommand.COMMAND_WORD, SyncCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD);
    }

    /**
     * Returns a defensive copy of {@code commandList}.
     */
    public List<String> getList() {
        return commandList;
    }
}
