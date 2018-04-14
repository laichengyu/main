//@@author laichengyu

package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.CommandTestUtil.ModelStub;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CoinBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyCoinBook;
import seedu.address.model.coin.Price;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;

public class SyncCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addAdditionalParams() {
        SyncCommand syncCommand = new SyncCommand();

        List<NameValuePair> testParams = new ArrayList<>();
        testParams.add(new BasicNameValuePair("fsyms", "BTC"));
        testParams.add(new BasicNameValuePair("limit", "168"));

        String type = "historical";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fsyms", "BTC"));
        syncCommand.addAdditionalParams(params, type);

        assertEquals(testParams, params);
    }

    @Test
    public void execute_throwsDuplicateCoinException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateCoinException();

        thrown.expect(CommandException.class);
        getSyncCommand(modelStub).execute();
    }

    @Test
    public void execute_throwsCoinNotFoundException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingCoinNotFoundException();

        thrown.expect(AssertionError.class);
        getSyncCommand(modelStub).execute();
    }

    /**
     * Generates a new SyncCommand.
     */
    private SyncCommand getSyncCommand(Model model) {
        SyncCommand command = new SyncCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A Model stub that always throw a DuplicateCoinException when trying to execute sync.
     */
    private class ModelStubThrowingDuplicateCoinException extends ModelStub {
        @Override
        public void syncAll(HashMap<String, Price> newPriceMetrics) throws DuplicateCoinException {
            throw new DuplicateCoinException();
        }

        @Override
        public ReadOnlyCoinBook getCoinBook() {
            return new CoinBook();
        }

        @Override
        public List<String> getCodeList() {
            return Arrays.asList("BTC", "ETH", "DOGE");
        }
    }

    /**
     * A Model stub that always throw a DuplicateCoinException when trying to execute sync.
     */
    private class ModelStubThrowingCoinNotFoundException extends ModelStub {
        @Override
        public void syncAll(HashMap<String, Price> newPriceMetrics) throws CoinNotFoundException {
            throw new CoinNotFoundException();
        }

        @Override
        public ReadOnlyCoinBook getCoinBook() {
            return new CoinBook();
        }

        @Override
        public List<String> getCodeList() {
            return Arrays.asList("BTC", "ETH", "DOGE");
        }
    }
}
