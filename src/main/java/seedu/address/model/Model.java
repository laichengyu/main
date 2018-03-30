package seedu.address.model;

import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonObject;

import javafx.collections.ObservableList;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Coin> PREDICATE_SHOW_ALL_COINS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyCoinBook newData);

    /** Returns the CoinBook */
    ReadOnlyCoinBook getCoinBook();

    /** Deletes the given coin. */
    void deleteCoin(Coin target) throws CoinNotFoundException;

    /** Adds the given coin */
    void addCoin(Coin coin) throws DuplicateCoinException;

    /**
     * Replaces the given coin {@code target} with {@code editedCoin}.
     *
     * @throws DuplicateCoinException if updating the coin's details causes the coin to be equivalent to
     *      another existing coin in the list.
     * @throws CoinNotFoundException if {@code target} could not be found in the list.
     */
    void updateCoin(Coin target, Coin editedCoin)
            throws DuplicateCoinException, CoinNotFoundException;

    /** Returns an unmodifiable view of the filtered coin list */
    ObservableList<Coin> getFilteredCoinList();

    //@@author laichengyu
    /** Returns an unmodifiable view of the code list */
    Set<String> getCodeList();

    /**
      * Syncs all coin data
      */
    void syncAll(JsonObject newData)
            throws DuplicateCoinException, CoinNotFoundException;
    //@@author

    /**
     * Updates the filter of the filtered coin list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCoinList(Predicate<Coin> predicate);

}
