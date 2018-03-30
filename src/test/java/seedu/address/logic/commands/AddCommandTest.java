package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.gson.JsonObject;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CoinBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyCoinBook;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;
import seedu.address.testutil.CoinBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullCoin_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_coinAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCoinAdded modelStub = new ModelStubAcceptingCoinAdded();
        Coin validCoin = new CoinBuilder().build();

        CommandResult commandResult = getAddCommandForCoin(validCoin, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validCoin), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validCoin), modelStub.coinsAdded);
    }

    @Test
    public void execute_duplicateCoin_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateCoinException();
        Coin validCoin = new CoinBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_COIN);

        getAddCommandForCoin(validCoin, modelStub).execute();
    }

    @Test
    public void equals() {
        Coin alice = new CoinBuilder().withName("Alice").build();
        Coin bob = new CoinBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different coin -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given coin.
     */
    private AddCommand getAddCommandForCoin(Coin coin, Model model) {
        AddCommand command = new AddCommand(coin);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addCoin(Coin coin) throws DuplicateCoinException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyCoinBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyCoinBook getCoinBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteCoin(Coin target) throws CoinNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateCoin(Coin target, Coin editedCoin)
                throws DuplicateCoinException {
            fail("This method should not be called.");
        }

        //@@author laichengyu
        @Override
        public void syncAll(JsonObject newData)
                throws DuplicateCoinException, CoinNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public Set<String> getCodeList() {
            fail("This method should not be called.");
            return null;
        }
        //@@author

        @Override
        public ObservableList<Coin> getFilteredCoinList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredCoinList(Predicate<Coin> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateCoinException when trying to add a coin.
     */
    private class ModelStubThrowingDuplicateCoinException extends ModelStub {
        @Override
        public void addCoin(Coin coin) throws DuplicateCoinException {
            throw new DuplicateCoinException();
        }

        @Override
        public ReadOnlyCoinBook getCoinBook() {
            return new CoinBook();
        }
    }

    /**
     * A Model stub that always accept the coin being added.
     */
    private class ModelStubAcceptingCoinAdded extends ModelStub {
        final ArrayList<Coin> coinsAdded = new ArrayList<>();

        @Override
        public void addCoin(Coin coin) throws DuplicateCoinException {
            requireNonNull(coin);
            coinsAdded.add(coin);
        }

        @Override
        public ReadOnlyCoinBook getCoinBook() {
            return new CoinBook();
        }
    }

}
