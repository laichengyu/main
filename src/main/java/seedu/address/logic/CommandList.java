//@@author laichengyu

package seedu.address.logic;

import java.util.Arrays;
import java.util.List;

/**
 * Stores a list of all available commands
 */
public class CommandList {
    private List<String> commandList;

    public CommandList() {
        commandList = Arrays.asList("help", "add", "buy", "sell", "delete", "clear", "tag", "list", "find", "view",
                "notify", "order", "history", "undo", "redo", "sync", "exit");
    }

    /**
     * Returns a defensive copy of {@code commandList}.
     */
    public List<String> getList() {
        return commandList;
    }
}
