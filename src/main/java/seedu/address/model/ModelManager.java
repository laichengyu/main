package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.CoinBookChangedEvent;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final CoinBook coinBook;
    private final FilteredList<Coin> filteredCoins;

    /**
     * Initializes a ModelManager with the given coinBook and userPrefs.
     */
    public ModelManager(ReadOnlyCoinBook coinBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(coinBook, userPrefs);

        logger.fine("Initializing with address book: " + coinBook + " and user prefs " + userPrefs);

        this.coinBook = new CoinBook(coinBook);
        filteredCoins = new FilteredList<>(this.coinBook.getCoinList());
    }

    public ModelManager() {
        this(new CoinBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyCoinBook newData) {
        coinBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyCoinBook getCoinBook() {
        return coinBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new CoinBookChangedEvent(coinBook));
    }

    @Override
    public synchronized void deleteCoin(Coin target) throws CoinNotFoundException {
        coinBook.removeCoin(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addCoin(Coin coin) throws DuplicateCoinException {
        coinBook.addCoin(coin);
        updateFilteredCoinList(PREDICATE_SHOW_ALL_COINS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateCoin(Coin target, Coin editedCoin)
            throws DuplicateCoinException, CoinNotFoundException {
        requireAllNonNull(target, editedCoin);

        coinBook.updateCoin(target, editedCoin);
        indicateAddressBookChanged();
    }

    //@@author laichengyu
    @Override
    public void syncAll(JsonObject newData)
            throws DuplicateCoinException, CoinNotFoundException {
        requireNonNull(newData);

        coinBook.syncAll(newData);
        indicateAddressBookChanged();
    }

    /** Returns an unmodifiable view of the code list */
    @Override
    public Set<String> getCodeList() {
        return coinBook.getCodeList();
    }
    //@@author

    //=========== Filtered Coin List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Coin} backed by the internal list of
     * {@code coinBook}
     */
    @Override
    public ObservableList<Coin> getFilteredCoinList() {
        return FXCollections.unmodifiableObservableList(filteredCoins);
    }

    @Override
    public void updateFilteredCoinList(Predicate<Coin> predicate) {
        requireNonNull(predicate);
        filteredCoins.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return coinBook.equals(other.coinBook)
                && filteredCoins.equals(other.filteredCoins);
    }

}
