package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.CoinBookChangedEvent;
import seedu.address.commons.events.model.RuleBookChangedEvent;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.Price;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;
import seedu.address.model.rule.Rule;
import seedu.address.model.rule.exceptions.DuplicateRuleException;
import seedu.address.model.rule.exceptions.RuleNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final RuleBook ruleBook;
    private final CoinBook coinBook;
    private final FilteredList<Coin> filteredCoins;

    /**
     * Initializes a ModelManager with the given coinBook and userPrefs.
     * For backwards compatibility.
     */
    public ModelManager(ReadOnlyCoinBook coinBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(coinBook, userPrefs);

        logger.fine("Initializing with coin book: " + coinBook
                + " empty rules, and user prefs " + userPrefs);

        this.coinBook = new CoinBook(coinBook);
        this.ruleBook = new RuleBook();
        filteredCoins = new FilteredList<>(this.coinBook.getCoinList());
    }

    /**
     * Initializes a ModelManager with the given coinBook and userPrefs.
     */
    public ModelManager(ReadOnlyCoinBook coinBook, ReadOnlyRuleBook ruleBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(coinBook, ruleBook, userPrefs);

        logger.fine("Initializing with coin book: " + coinBook
                + " rules: " + ruleBook
                + " and user prefs " + userPrefs);

        this.coinBook = new CoinBook(coinBook);
        this.ruleBook = new RuleBook(ruleBook);
        filteredCoins = new FilteredList<>(this.coinBook.getCoinList());
    }

    public ModelManager() {
        this(new CoinBook(), new RuleBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyCoinBook newData) {
        coinBook.resetData(newData);
        indicateCoinBookChanged();
    }

    @Override
    public ReadOnlyCoinBook getCoinBook() {
        return coinBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateCoinBookChanged() {
        raise(new CoinBookChangedEvent(coinBook));
    }

    @Override
    public synchronized void deleteCoin(Coin target) throws CoinNotFoundException {
        coinBook.removeCoin(target);
        indicateCoinBookChanged();
    }

    @Override
    public synchronized void addCoin(Coin coin) throws DuplicateCoinException {
        coinBook.addCoin(coin);
        updateFilteredCoinList(PREDICATE_SHOW_ALL_COINS);
        indicateCoinBookChanged();
    }

    @Override
    public void updateCoin(Coin target, Coin editedCoin)
            throws DuplicateCoinException, CoinNotFoundException {
        requireAllNonNull(target, editedCoin);

        coinBook.updateCoin(target, editedCoin);
        indicateCoinBookChanged();
    }

    //@@author laichengyu
    @Override
    public void syncAll(HashMap<String, Price> newPriceMetrics)
            throws DuplicateCoinException, CoinNotFoundException {
        requireNonNull(newPriceMetrics);

        coinBook.syncAll(newPriceMetrics);
        indicateCoinBookChanged();
    }

    /** Returns an unmodifiable view of the code list */
    @Override
    public List<String> getCodeList() {
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


    //=========== Rule Book =============================================================

    /** Raises an event to indicate the model has changed */
    private void indicateRuleBookChanged() {
        raise(new RuleBookChangedEvent(ruleBook));
    }

    @Override
    public synchronized void addRule(Rule rule) throws DuplicateRuleException {
        ruleBook.addRule(rule);
        indicateRuleBookChanged();
    }

    @Override
    public void updateRule(Rule target, Rule editedRule)
            throws DuplicateRuleException, RuleNotFoundException {
        requireAllNonNull(target, editedRule);

        ruleBook.updateRule(target, editedRule);
        indicateRuleBookChanged();
    }

    @Override
    public synchronized void deleteRule(Rule target) throws RuleNotFoundException {
        ruleBook.removeRule(target);
        indicateRuleBookChanged();
    }

    @Override
    public ObservableList<Rule> getRuleList() {
        return FXCollections.unmodifiableObservableList(ruleBook.getRuleList());
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
                && filteredCoins.equals(other.filteredCoins)
                && ruleBook.equals(other.ruleBook);
    }

}
