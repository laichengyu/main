package seedu.address.model;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.Price;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;
import seedu.address.model.rule.Rule;
import seedu.address.model.rule.exceptions.DuplicateRuleException;
import seedu.address.model.rule.exceptions.RuleNotFoundException;

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
    List<String> getCodeList();

    /**
      * Syncs all coin data
      */
    void syncAll(HashMap<String, Price> newPriceMetrics)
            throws DuplicateCoinException, CoinNotFoundException;
    //@@author

    /**
     * Updates the filter of the filtered coin list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCoinList(Predicate<Coin> predicate);


    /** Deletes the given rule. */
    void deleteRule(Rule target) throws RuleNotFoundException;

    /** Adds the given rule */
    void addRule(Rule rule) throws DuplicateRuleException;

    /**
     * Replaces the given rule {@code target} with {@code editedRule}.
     *
     * @throws DuplicateRuleException if updating the rule's details causes the rule to be equivalent to
     *      another existing rule in the list.
     * @throws RuleNotFoundException if {@code target} could not be found in the list.
     */
    void updateRule(Rule target, Rule editedRule)
            throws DuplicateRuleException, RuleNotFoundException;

    /** Returns an unmodifiable view of the rule list */
    ObservableList<Rule> getRuleList();

}
