package seedu.address.testutil;

import java.io.File;
import java.io.IOException;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.Model;
import seedu.address.model.coin.Coin;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting string.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    /**
     * Returns the middle index of the coin in the {@code model}'s coin list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getCoinBook().getCoinList().size() / 2);
    }

    /**
     * Returns the last index of the coin in the {@code model}'s coin list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getCoinBook().getCoinList().size());
    }

    /**
     * Returns the coin in the {@code model}'s coin list at {@code index}.
     */
    public static Coin getCoin(Model model, Index index) {
        return model.getCoinBook().getCoinList().get(index.getZeroBased());
    }
}
