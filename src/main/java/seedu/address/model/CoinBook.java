package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.coin.Coin;
import seedu.address.model.coin.Price;
import seedu.address.model.coin.UniqueCoinList;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class CoinBook implements ReadOnlyCoinBook {

    private final UniqueCoinList coins;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        coins = new UniqueCoinList();
        tags = new UniqueTagList();
    }

    public CoinBook() {}

    /**
     * Creates an CoinBook using the Coins and Tags in the {@code toBeCopied}
     */
    public CoinBook(ReadOnlyCoinBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setCoins(List<Coin> coins) throws DuplicateCoinException {
        this.coins.setCoins(coins);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code CoinBook} with {@code newData}.
     */
    public void resetData(ReadOnlyCoinBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Coin> syncedCoinList = newData.getCoinList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());

        try {
            setCoins(syncedCoinList);
        } catch (DuplicateCoinException e) {
            throw new AssertionError("AddressBooks should not have duplicate coins");
        }
    }

    //// coin-level operations

    /**
     * Adds a coin to the address book.
     * Also checks the new coin's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the coin to point to those in {@link #tags}.
     *
     * @throws DuplicateCoinException if an equivalent coin already exists.
     */
    public void addCoin(Coin c) throws DuplicateCoinException {
        Coin coin = syncWithMasterTagList(c);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any coin
        // in the coin list.
        coins.add(coin);
    }

    /**
     * Replaces the given coin {@code target} in the list with {@code editedCoin}.
     * {@code CoinBook}'s tag list will be updated with the tags of {@code editedCoin}.
     *
     * @throws DuplicateCoinException if updating the coin's details causes the coin to be equivalent to
     *      another existing coin in the list.
     * @throws CoinNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Coin)
     */
    public void updateCoin(Coin target, Coin editedCoin)
            throws DuplicateCoinException, CoinNotFoundException {
        requireNonNull(editedCoin);

        Coin syncedEditedCoin = syncWithMasterTagList(editedCoin);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any coin
        // in the coin list.
        coins.setCoin(target, syncedEditedCoin);
    }

    //@@author laichengyu
    /**
     * Replaces every coin in the list that has a price change in {@code newData} with {@code updatedCoin}.
     * {@code CoinBook}'s tag list will be updated with the tags of {@code updatedCoin}.
     *
     * @throws DuplicateCoinException if updating the coin's details causes the coin to be equivalent to
     *      another existing coin in the list.
     * @throws CoinNotFoundException if {@code coin} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Coin)
     */
    public void syncAll(HashMap<String, Price> newPriceMetrics)
            throws DuplicateCoinException, CoinNotFoundException {
        requireNonNull(newPriceMetrics);

        for (Coin coin : coins) {
            String code = coin.getCode().toString();
            Price newPrice = newPriceMetrics.get(code);
            if (newPrice == null) {
                continue;
            }
            Coin updatedCoin = new Coin(coin, newPrice);
            updateCoin(coin, updatedCoin);
        }
    }
    //@@author

    /**
     *  Updates the master tag list to include tags in {@code coin} that are not in the list.
     *  @return a copy of this {@code coin} such that every tag in this coin points to a Tag object in the master
     *  list.
     */
    private Coin syncWithMasterTagList(Coin coin) {
        final UniqueTagList coinTags = new UniqueTagList(coin.getTags());
        tags.mergeFrom(coinTags);

        // Create map with values = tag object references in the master list
        // used for checking coin tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of coin tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        coinTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Coin(
                coin, coin.getCode(), correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code CoinBook}.
     * @throws CoinNotFoundException if the {@code key} is not in this {@code CoinBook}.
     */
    public boolean removeCoin(Coin key) throws CoinNotFoundException {
        if (coins.remove(key)) {
            return true;
        } else {
            throw new CoinNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return coins.asObservableList().size() + " coins, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Coin> getCoinList() {
        return coins.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    //@@author laichengyu
    @Override
    public List<String> getCodeList() {
        return Collections.unmodifiableList(coins.asObservableList().stream()
                .map(coin -> coin.getCode().toString())
                .collect(Collectors.toList()));
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CoinBook // instanceof handles nulls
                && this.coins.equals(((CoinBook) other).coins)
                && this.tags.equalsOrderInsensitive(((CoinBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(coins, tags);
    }
}
